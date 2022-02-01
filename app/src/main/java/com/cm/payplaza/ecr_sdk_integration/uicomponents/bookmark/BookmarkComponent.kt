package com.cm.payplaza.ecr_sdk_integration.uicomponents.bookmark

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.cm.payplaza.ecr_sdk_integration.R
import org.koin.core.component.KoinComponent
import timber.log.Timber

class BookmarkComponent(context: Context, attributeSet: AttributeSet) : FrameLayout(
    context,
    attributeSet
), KoinComponent {
    private var numberBookmarks: Int = 0
    private var selectedBookmark: Int = 0
    private var highlightBackground: Drawable? = null
    private var bulletBackground: Drawable? = null
    private var bulletBackgroundInactive: Drawable? = null
    private var bulletBackgroundSelected: Drawable? = null
    private var dividerBackground: Drawable? = null
    private var appearanceTextBookmark: Int = 0
    private var margin: Int = 0
    private var padding: Int = 0
    private var dividerWidth: Int = 0
    private var bulletGlyphs: MutableList<Drawable> =  mutableListOf()
    private var bulletSize: Int = 0
    private var selectedLabelWidth: Int = 0
    private var bullets: MutableList<TextView> = mutableListOf()
    private var labels: MutableList<TextView> = mutableListOf()
    private var labelLayoutParams: MutableList<LinearLayout.LayoutParams> =  mutableListOf()
    private var highlight: View? = null
    private var highlightLayoutParams: LayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    private var accordion: LinearLayout? = null
    private var selectedBookmarkStringResourceId: Int? = null

    init {
        retrieveAttributes(context, attributeSet)
        createUiElements(context)
    }

    fun setSelectedBookmark(bookmark: Int, stringResourceId: Int) {
        Timber.d("setSelectedBookmark - bookmark $bookmark - stringResourceId $stringResourceId")
        validateSelection(bookmark)
        labels[bookmark - 1].text = resources.getString(stringResourceId)
        selectedBookmarkStringResourceId = stringResourceId
        if(bookmark != selectedBookmark) {
            val startMarginLeft = highlightLayoutParams.leftMargin
            val startMarginRight = highlightLayoutParams.rightMargin
            val endMarginLeft = calculateHighlightLeftMargin(bookmark)
            val endMarginRight = calculateHighlightRightMargin(bookmark)
            val oldBookmark = selectedBookmark
            selectedBookmark = bookmark
            setCheckmarks(bookmark)
            animation = object: Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    highlightLayoutParams.leftMargin = (startMarginLeft + interpolatedTime * (endMarginLeft - startMarginLeft)).toInt()
                    highlightLayoutParams.rightMargin = (startMarginRight + interpolatedTime * (endMarginRight - startMarginRight)).toInt()
                    var newWidth: Int
                    var newAlpha: Float
                    for(i in 1..numberBookmarks) {
                        newWidth = 0
                        newAlpha = 0f
                        if(i == bookmark) {
                            newWidth = (selectedLabelWidth * interpolatedTime).toInt()
                            newAlpha = interpolatedTime
                        } else if (i == oldBookmark) {
                            newWidth = selectedLabelWidth - (selectedLabelWidth * interpolatedTime).toInt()
                            newAlpha = 1 - interpolatedTime
                        }
                        labelLayoutParams[i-1].width = newWidth
                        labels[i-1].alpha = newAlpha
                    }
                    highlight?.requestLayout()
                    accordion?.requestLayout()
                }
            }
            animation.duration = 200
            startAnimation(animation)
        }
    }

    private fun retrieveAttributes(context: Context, attributeSet: AttributeSet) {
        Timber.d("retrieveAttributes")
        val params = generateLayoutParams(attributeSet)
        val totalWidth = params.width - paddingLeft - paddingRight
        val totalHeight = params.height - paddingTop - paddingBottom
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.BookmarkComponent)
        var glyphs: TypedArray? = null

        try {
            numberBookmarks = attrs.getInteger(R.styleable.BookmarkComponent_numberBookmarks, 2)
            selectedBookmark = attrs.getInteger(R.styleable.BookmarkComponent_selectedBookmark, 1)
            highlightBackground = attrs.getDrawable(R.styleable.BookmarkComponent_backgroundHighlight)
            bulletBackground = attrs.getDrawable(R.styleable.BookmarkComponent_backgroundBullet)
            bulletBackgroundInactive = attrs.getDrawable(R.styleable.BookmarkComponent_backgroundBulletInactive)
            bulletBackgroundSelected = attrs.getDrawable(R.styleable.BookmarkComponent_backgroundBulletSelected)
            dividerBackground = attrs.getDrawable(R.styleable.BookmarkComponent_backgroundDivider)
            appearanceTextBookmark = attrs.getResourceId(R.styleable.BookmarkComponent_appearanceTextBookmark, R.style.TextAppearance_AppCompat)
            margin = attrs.getDimensionPixelSize(R.styleable.BookmarkComponent_margin, 2)
            padding = attrs.getDimensionPixelSize(R.styleable.BookmarkComponent_padding, 2)
            dividerWidth = attrs.getDimensionPixelSize(R.styleable.BookmarkComponent_widthDivider, 8)
            val arrayId = attrs.getResourceId(R.styleable.BookmarkComponent_glyphs, 0)
            glyphs = resources.obtainTypedArray(arrayId)
            for(i in 0 until glyphs.length()) {
                val drawable = glyphs.getDrawable(i) as Drawable
                bulletGlyphs.add(drawable)
            }
        } catch(e: Exception) {
            Timber.e(e.toString())
        } finally {
            attrs.recycle()
            glyphs?.recycle()
        }
        validateSelection(selectedBookmark)
        bulletSize = totalHeight - 2 * padding
        selectedLabelWidth = totalWidth - 2 * padding - numberBookmarks * bulletSize - (numberBookmarks - 1) * (dividerWidth + 2 * margin)
    }

    private fun validateSelection(bookmark: Int) {
        Timber.d("validateSelection - $bookmark")
        if(bookmark > numberBookmarks) throw RuntimeException(String.format(context.getString(R.string.bookmark_overflow), bookmark, numberBookmarks))
        if(bookmark < 1) throw RuntimeException(String.format(context.getString(R.string.bookmark_zero), bookmark, numberBookmarks))
    }

    private fun createUiElements(context: Context) {
        Timber.d("createUiElements")
        // The highlight slider drawn under the active bookmark
        highlight = View(context)
        highlightLayoutParams.leftMargin = calculateHighlightLeftMargin(selectedBookmark)
        highlightLayoutParams.rightMargin = calculateHighlightRightMargin(selectedBookmark)
        highlight?.background = highlightBackground
        highlight?.layoutParams = highlightLayoutParams
        addView(highlight)
        accordion = LinearLayout(context)
        accordion?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        accordion?.setPadding(padding, padding, padding, padding)
        addView(accordion)
        // Create bullets, labels, and dividers
        for(i in 0..numberBookmarks) {
            // Divider
            if(i > 0) {
                val divider = View(context)
                val dividerParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                divider.background = dividerBackground
                dividerParams.width = dividerWidth
                dividerParams.setMargins(margin, 0, margin, 0)
                divider.layoutParams = dividerParams
                accordion?.addView(divider)
            }
            // Bullet
            val bulletParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
            bulletParams.height = bulletSize
            bulletParams.width = bulletSize
            val imageView = TextView(context)
            imageView.background = bulletBackground
            imageView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            imageView.textSize = resources.getDimension(R.dimen.bullet_number)
            imageView.setTextColor(resources.getColor(R.color.windowBackgroundColor, null))
            imageView.layoutParams = bulletParams
            bullets.add(imageView)
            accordion?.addView(bullets[i])
            // Label
            val newParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT)
            if (i == (selectedBookmark - 1)) newParams.width = selectedLabelWidth
            labelLayoutParams.add(newParams)
            val newTextView = TextView(context)
            newTextView.layoutParams = newParams
            newTextView.gravity = Gravity.CENTER_VERTICAL
            newTextView.setSingleLine()
            newTextView.setTextAppearance(appearanceTextBookmark)
            newTextView.setPadding(padding, 0, 0, 0)
            newTextView.text = getPageString(i)
            labels.add(newTextView)
            accordion?.addView(labels[i])
        }
        setCheckmarks(selectedBookmark)
    }

    private fun getPageString(page: Int): String {
        Timber.d("getPageString - $page")
        return String.format(resources.getString(R.string.bookmark_page), (page+1))
    }

    private fun calculateHighlightLeftMargin(bookmark: Int): Int {
        Timber.d("calculateHighlightLeftMargin - $bookmark")
        return if(bookmark == 1) 0 else padding + (bookmark - 1) * (bulletSize + 2 * margin + dividerWidth) - margin - dividerWidth / 2
    }
    private fun calculateHighlightRightMargin(bookmark: Int): Int {
        Timber.d("calculateHighlightRightMargin - $bookmark")
        return if(bookmark == numberBookmarks) 0 else padding + (numberBookmarks - bookmark) * (bulletSize + 2 * margin + dividerWidth) - margin - dividerWidth / 2
    }

    private fun setCheckmarks(bookmark: Int) {
        Timber.d("setCheckmarks - $bookmark")
        for(i in 1..numberBookmarks) {
            val bullet = bullets[i-1]
            when {
                i < bookmark -> {
                    bullet.text = "\u2713" // Check character
                    bullet.background = bulletBackgroundSelected
                }
                i > bookmark -> {
                    bullet.text = i.toString()
                    bullet.background = bulletBackgroundInactive
                }
                else -> {
                    bullet.text = i.toString()
                    bullet.background = bulletBackground
                }
            }
        }
    }
}