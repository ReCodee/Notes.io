package com.example.notesio

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.util.Log


object HtmlParser {
        fun toHtml(text: Spannable): String {
            Log.d("Checking HTML Parser", text.toString())
            val ssBuilder = SpannableStringBuilder(text)
            var start: Int
            var end: Int

            // Replace Style spans with <b></b> or <i></i>
            val styleSpans = ssBuilder.getSpans(
                0, text.length,
                StyleSpan::class.java
            )
            for (i in styleSpans.indices.reversed()) {
                val span = styleSpans[i]
                start = ssBuilder.getSpanStart(span)
                end = ssBuilder.getSpanEnd(span)
                ssBuilder.removeSpan(span)
                if (span.style == Typeface.BOLD) {
                    ssBuilder.insert(start, "<b>")
                    ssBuilder.insert(end + 3, "</b>")
                } else if (span.style == Typeface.ITALIC) {
                    ssBuilder.insert(start, "<i>")
                    ssBuilder.insert(end + 3, "</i>")
                }
            }

            // Replace underline spans with <u></u>
            val underSpans: Array<UnderlineSpan> = ssBuilder.getSpans(
                0, ssBuilder.length,
                UnderlineSpan::class.java
            )
            for (i in underSpans.indices.reversed()) {
                val span: UnderlineSpan = underSpans[i]
                start = ssBuilder.getSpanStart(span)
                end = ssBuilder.getSpanEnd(span)
                ssBuilder.removeSpan(span)
                ssBuilder.insert(start, "<u>")
                ssBuilder.insert(end + 3, "</u>")
            }

            // Replace Strikethrough spans with <s></s>
            val strikeSpans: Array<StrikethroughSpan> = ssBuilder.getSpans(
                0, ssBuilder.length,
                StrikethroughSpan::class.java
            )
            for (i in strikeSpans.indices.reversed()) {
                val span: StrikethroughSpan = strikeSpans[i]
                start = ssBuilder.getSpanStart(span)
                end = ssBuilder.getSpanEnd(span)
                ssBuilder.removeSpan(span)
                ssBuilder.insert(start, "<s>")
                ssBuilder.insert(end + 3, "</s>")
            }

            // Replace Bullet spans with <ui><li><li></ui>
            val bulletSpans: Array<BulletSpan> = ssBuilder.getSpans(
                0, ssBuilder.length,
                BulletSpan::class.java
            )
            for (i in bulletSpans.indices.reversed()) {
                val span: BulletSpan = bulletSpans[i]
                start = ssBuilder.getSpanStart(span)
                end = ssBuilder.getSpanEnd(span)
                ssBuilder.removeSpan(span)
                ssBuilder.insert(start, "<li>")
                ssBuilder.insert(end + 4, "</li>")
            }
            val urlSpans: Array<URLSpan> = ssBuilder.getSpans(
                0, ssBuilder.length,
                URLSpan::class.java
            )
            for (i in urlSpans.indices.reversed()) {
                val span: URLSpan = urlSpans[i]
                start = ssBuilder.getSpanStart(span)
                end = ssBuilder.getSpanEnd(span)
                val link = span.url
                ssBuilder.removeSpan(span)
                Log.d("Checking URL Span", span.toString())
                Log.d("Checking Span Builder start", start.toString())
                Log.d("Checking Span Builder end", end.toString())
                Log.d("Checking Span Builder", ssBuilder.toString())
                val tagStart = "<a href='${link}' target='_blank'>"
                ssBuilder.insert(start, tagStart)
                ssBuilder.insert(end + tagStart.length, "</a>")
            }


            //replace(ssBuilder, '\n', "<br/>")
            Log.d("Checking HTML Parser", ssBuilder.toString())
            return ssBuilder.toString()
        }

        private fun replace(b: SpannableStringBuilder, oldChar: Char, newStr: String) {
            for (i in b.length - 1 downTo 0) {
                if (b[i] == oldChar) {
                    b.replace(i, i + 1, newStr)
                }
            }
        }
    }
