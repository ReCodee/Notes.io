package com.example.notesio.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.notesio.R
import com.example.notesio.databinding.FragmentEditorScreenBinding
import io.github.mthli.knife.KnifeText


class EditorScreen : Fragment() {

    private var _binding: FragmentEditorScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var knife : KnifeText
    private val args : EditorScreenArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditorScreenBinding
            .inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        knife = binding.knife
        binding.knife.setText(args.note.body)
        setupBold()
        setupItalic()
        setupUnderline()
        setupStrikethrough()
        setupBullet()
        setupQuote()
        setupLink()
        setupClear()

    }

    private fun setupBold() {
        val bold = binding.bold
        bold.setOnClickListener { knife.bold(!knife.contains(KnifeText.FORMAT_BOLD)) }
        bold.setOnLongClickListener {
            Toast.makeText(context, "Bold", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupItalic() {
        val italic = binding.italic
        italic.setOnClickListener { knife.italic(!knife.contains(KnifeText.FORMAT_ITALIC)) }
        italic.setOnLongClickListener {
            Toast.makeText(context, "Italic", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupUnderline() {
        val underline = binding.underline
        underline.setOnClickListener { knife.underline(!knife.contains(KnifeText.FORMAT_UNDERLINED)) }
        underline.setOnLongClickListener {
            Toast.makeText(context, "Underline", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupStrikethrough() {
        val strikethrough = binding.strikethrough
        strikethrough.setOnClickListener { knife.strikethrough(!knife.contains(KnifeText.FORMAT_STRIKETHROUGH)) }
        strikethrough.setOnLongClickListener {
            Toast.makeText(context, "Strike Through", Toast.LENGTH_SHORT)
                .show()
            true
        }
    }

    private fun setupBullet() {
        val bullet = binding.bullet
        bullet.setOnClickListener { knife.bullet(!knife.contains(KnifeText.FORMAT_BULLET)) }
        bullet.setOnLongClickListener {
            Toast.makeText(context, "Bullet", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupQuote() {
        val quote = binding.quote
        quote.setOnClickListener { knife.quote(!knife.contains(KnifeText.FORMAT_QUOTE)) }
        quote.setOnLongClickListener {
            Toast.makeText(context, "Quote", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupLink() {
        val link = binding.link
        //link.setOnClickListener { showLinkDialog() }
        link.setOnLongClickListener {
            Toast.makeText(context, "Insert Link", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupClear() {
        val clear = binding.clear
        clear.setOnClickListener { knife.clearFormats() }
        clear.setOnLongClickListener {
            Toast.makeText(context, "Format Clear", Toast.LENGTH_SHORT)
                .show()
            true
        }
    }

}