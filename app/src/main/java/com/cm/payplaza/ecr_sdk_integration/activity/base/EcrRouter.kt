package com.cm.payplaza.ecr_sdk_integration.activity.base

import android.content.Context
import android.content.Intent
import com.cm.payplaza.ecr_sdk_integration.activity.lastReceipt.LastReceiptActivity
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.refund.RefundActivity
import com.cm.payplaza.ecr_sdk_integration.activity.statuses.StatusesActivity
import com.cm.payplaza.ecr_sdk_integration.activity.totals.TotalsActivity
import com.cm.payplaza.ecr_sdk_integration.activity.transactionResult.TransactionResultActivity
import timber.log.Timber

class EcrRouter {
    companion object {
        fun goToPaymentActivity(context: Context) {
            Timber.d("goToPaymentActivity")
            val intent = Intent(context, PaymentActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun goToRefundActivity(context: Context) {
            Timber.d("goToRefundActivity")
            val intent = Intent(context, RefundActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun goToLastReceipt(context: Context) {
            Timber.d("goToLastReceipt")
            val intent = Intent(context, LastReceiptActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun goToTotals(context: Context) {
            Timber.d("goToTotals")
            val intent = Intent(context, TotalsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun goToTransactionResultActivity(context: Context) {
            Timber.d("goToTotals")
            val intent = Intent(context, TransactionResultActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun goToStatusesActivity(context: Context) {
            Timber.d("goToStatusesFragment")
            val intent = Intent(context, StatusesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}