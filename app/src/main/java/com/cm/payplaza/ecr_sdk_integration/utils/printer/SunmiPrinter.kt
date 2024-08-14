package com.cm.payplaza.ecr_sdk_integration.utils.printer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.IBinder
import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import com.sunmi.peripheral.printer.InnerPrinterCallback
import com.sunmi.peripheral.printer.InnerPrinterException
import com.sunmi.peripheral.printer.InnerResultCallback
import com.sunmi.peripheral.printer.SunmiPrinterService
import timber.log.Timber

class SunmiPrinter(
    private val context: Context
): InnerPrinterCallback() {
    companion object {
        private const val PACKAGE: String = "woyou.aidlservice.jiuiv5"
        private const val ACTION = "woyou.aidlservice.jiuiv5.IWoyouService"
        private const val newWidth = 348
    }

    private var printerService: SunmiPrinterService? = null
    private lateinit var printCallback: InnerResultCallback
    private lateinit var printerConnection: ServiceConnection
    val printerStatus: PrinterStatus
        get() = printerService?.let { service ->
            decodeLowLevelStatus(SunmiPrinterStatus.getByValue(service.updatePrinterState()) ?: SunmiPrinterStatus.NO_PRINTER_DETECTED)
        } ?: decodeLowLevelStatus(SunmiPrinterStatus.NO_PRINTER_DETECTED)
    private var isPrinterBinded = false

    init {
        try { printCallback = setUpPrintCallback() }
        catch(e: InnerPrinterException) {
            Timber.e(e.toString())
        }
    }

    fun bindPrinter(updateView: (Boolean) -> Any) {
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Timber.d("onServiceConnected - $name")
                serviceConnected(service)
                isPrinterBinded = true
                updateView(true)
            }
            override fun onServiceDisconnected(name: ComponentName?){
                Timber.d("onServiceDisconnected - $name")
                updateView(isPrinterBinded)
            }
        }
        val intent = Intent()
        intent.setPackage(PACKAGE)
        intent.action = ACTION
        if(!context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {
            printerConnection = serviceConnection
            printerConnection.onServiceDisconnected(null)
        }
    }

    fun unBindPrinter() {
        try { context.unbindService(printerConnection) }
        catch(e: Exception) { Timber.d("Printer not available") }
        isPrinterBinded = false
    }

    fun serviceConnected(service: IBinder?) {
        printerService = SunmiPrinterService.Stub.asInterface(service)
    }

    fun isPrinterOutOfPaper(): Boolean {
        return printerStatus == PrinterStatus.OUT_OF_PAPER
    }

    fun printReceipt(receipt: Receipt) {
        when(printerStatus) {
            PrinterStatus.AVAILABLE -> {
                printReceiptBeforeSignature(receipt)
                receipt.signature?.let {
                    printReceiptSignature(receipt)
                    printReceiptAfterSignature(receipt)
                }
                doPrint()
            }
            else -> Timber.e("Printer is not available right now")
        }
    }

    private fun doPrint() {
        printerService?.lineWrap(4, printCallback)
        printerService?.exitPrinterBufferWithCallback(true, printCallback)
    }

    private fun printReceiptBeforeSignature(receipt: Receipt) {
        receipt.receiptLines?.let {
            val fontSize = calculateFontSize(it)
            setLineHeight()
            printerService?.enterPrinterBuffer(true)
            val lines = receipt.getLinesBeforeSignature()
            lines.let { l ->
                l.forEach { line ->
                    printerService?.printTextWithFont(line, "", fontSize.toFloat(), printCallback)
                    printerService?.lineWrap(1, printCallback)
                }
            }
        }
    }

    private fun printReceiptSignature(receipt: Receipt) {
        receipt.signature?.let {
            val bmp: Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            val scaledHeight: Int = (bmp.height * newWidth) / bmp.width
            val scaledSignature = BitmapUtils.scale(bmp, newWidth, scaledHeight)
            val coloredSignature = BitmapUtils.replaceColor(scaledSignature, Color.TRANSPARENT, Color.WHITE)
            printerService?.setAlignment(1, printCallback)
            printerService?.printBitmap(coloredSignature, printCallback)
            printerService?.setAlignment(0, printCallback)
        }
    }

    private fun printReceiptAfterSignature(receipt: Receipt) {
        receipt.receiptLines?.let {
            val fontSize = calculateFontSize(it)
            setLineHeight()
            val lines = receipt.getLinesAfterSignature()
            lines.let { l ->
                l.forEach { line ->
                    printerService?.printTextWithFont(line, "", fontSize.toFloat(), printCallback)
                    printerService?.lineWrap(1, printCallback)
                }
            }
        }
    }

    private fun calculateFontSize(lines: Array<String>): Int {
        var longestLine = 0
        var fontSize = PrinterFonts.fonts[0].fontSize
        lines.forEach { if(it.length > longestLine) longestLine = it.length }
        PrinterFonts.fonts.forEach {
            if(longestLine >= it.minWidth && longestLine <= it.maxWidth) { fontSize = it.fontSize }
        }
        return fontSize
    }

    private fun setLineHeight() {
        val command = byteArrayOf(0x1B, 0x33, 0x12)
        try {
            printerService?.sendRAWData(command, null)
        } catch(e: Exception) {
            Timber.e(e.toString())
        }
    }

    private fun setUpPrintCallback(): InnerResultCallback {
        return object : InnerResultCallback() {
            override fun onRunResult(p0: Boolean) { Timber.d("onRunResult: $p0") }
            override fun onReturnString(p0: String?) { Timber.d("onReturnString: $p0") }
            override fun onRaiseException(p0: Int, p1: String?) { Timber.d("onRaiseException: $p0 - $p1") }
            override fun onPrintResult(p0: Int, p1: String?) { Timber.d("onPrintResult: $p0 - $p1") }
        }
    }

    private fun decodeLowLevelStatus(status: SunmiPrinterStatus): PrinterStatus {
        return when (status) {
            SunmiPrinterStatus.NORMAL -> PrinterStatus.AVAILABLE
            SunmiPrinterStatus.OUT_OF_PAPER -> PrinterStatus.OUT_OF_PAPER
            SunmiPrinterStatus.OVERHEATED -> PrinterStatus.UNAVAILABLE
            SunmiPrinterStatus.TRAY_OPEN -> PrinterStatus.UNAVAILABLE
            SunmiPrinterStatus.NO_PRINTER_DETECTED -> PrinterStatus.UNAVAILABLE
            SunmiPrinterStatus.UPDATE_STATUS, SunmiPrinterStatus.GET_STATUS_EXCEPTION, SunmiPrinterStatus.CUTTER_ABNORMAL, SunmiPrinterStatus.CUTTER_RECOVERY, SunmiPrinterStatus.NO_BLACK_MARK -> PrinterStatus.UNAVAILABLE
        }
    }

    override fun onConnected(p0: SunmiPrinterService?) { Timber.e("Sunmi Printer service Connected") }

    override fun onDisconnected() { Timber.d("Sunmi Printer service Disconnected") }
}