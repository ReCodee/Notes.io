package com.example.notesio.editor

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.util.LinkifyCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notesio.MainActivity
import com.example.notesio.R
import com.example.notesio.backend.model.Note
import com.example.notesio.backend.viewmodel.NoteViewModel
import com.example.notesio.databinding.FragmentEditorScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors


@AndroidEntryPoint
class EditorScreen : Fragment() {

    private var _binding: FragmentEditorScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var knife : EditText
    private val noteViewModel : NoteViewModel by viewModels()
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

    override fun onResume() {
        super.onResume()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val notes : MutableList<Note>? = noteViewModel.data.value?.toMutableList()
        //Log.d("Checking Other Way Round", notes?.size.toString())
        knife = binding.knife

        Log.d("Checking Safe Args", args.note.data.toString())
        //binding.knife.setText(Html.fromHtml(args.note.data, HtmlCompat.FROM_HTML_MODE_LEGACY))
        knife.setText(args.note.data)
        binding.titleText.setText(args.note.title)
        knife.movementMethod = LinkMovementMethod.getInstance()
        knife.doAfterTextChanged { editable ->
            // Add new links
            LinkifyCompat.addLinks(editable ?: return@doAfterTextChanged,
                Linkify.WEB_URLS or Linkify.EMAIL_ADDRESSES)
        }
        knife.setLinkTextColor(resources.getColor(R.color.orange, activity?.theme))
        //Log.d("Checking Live Data", noteData[args.index].data.toString())
        /*setupBold()
        setupItalic()
        setupUnderline()
        setupStrikethrough()
        setupBullet()
        setupQuote()
        setupLink()*/
        //setupClear()

        binding.deleteButton.setOnClickListener {
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            val note: Note = Note()
            if ( args.note.data == "" ) {
              Toast.makeText(context, "Cannot delete an unsaved note", Toast.LENGTH_SHORT).show()
            } else {
                note.email = args.note.email
                note.title = binding.titleText.text.toString()
                note.data = knife.text.toString()
                note.id = args.note.id
                // notes?.add(note)
                // noteViewModel.data.value = notes?.toList()
                executor.execute {
                    Log.d("Note Data 2", note.data.toString())
                    noteViewModel.deleteNote(note)
                    handler.post {
                        findNavController().navigate(R.id.action_editor_to_home)
                    }
                }
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_editor_to_home)
        }

        binding.saveButton.setOnClickListener {
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            val note: Note = Note()
            if ( args.note.data == "" ) {
                //val notes = noteViewModel.data.value?.toMutableList()
                note.email = MainActivity.auth.currentUser?.email.toString()
                Log.d("Checking Get Text", knife.text.toString())
                note.title = binding.titleText.text.toString()
                note.data = knife.text.toString()
                note.id = MainActivity.dbRef.push().key!!
                //notes?.add(note)
                //noteViewModel.data.value = notes?.toList()
                if ( note.title.isNotEmpty() && note.data.isNotEmpty()) {
                    executor.execute {
                        Log.d("Note Data 1", note.data.toString())
                        noteViewModel.insertNote(note, onSuccess = {
                            Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_LONG).show()
                        }, onError = {
                            Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_LONG).show()
                        })
                        handler.post {
                            findNavController().navigate(R.id.action_editor_to_home)
                        }
                    }
                } else {
                    Toast.makeText(context, "Cannot save an empty note", Toast.LENGTH_SHORT).show()
                }

            } else {
                    note.email = args.note.email
                    note.title = binding.titleText.text.toString()
                    note.data = knife.text.toString()
                    note.id = args.note.id
               // notes?.add(note)
               // noteViewModel.data.value = notes?.toList()
                executor.execute {
                    Log.d("Note Data 2", note.data.toString())
                    noteViewModel.updateNote(note)
                    handler.post {
                        findNavController().navigate(R.id.action_editor_to_home)
                    }
                }
            }
        }
    }

    /*private fun setupBold() {
        val bold = binding.bold
        bold.setOnClickListener { knife.bold(!knife.contains(KnifeText.FORMAT_BOLD)) }
        *//*bold.setOnClickListener{
            val start = knife.selectionStart
            val end = knife.selectionEnd
            val wholeText: String = knife.text.toString()
            val selectedText: String = knife.text.substring(start, end)

            val textItalic = "<b>$selectedText</b>"
            Log.d("Checking Bold", wholeText.replace(selectedText, textItalic))
            knife.setText(Html.fromHtml( wholeText.replace(selectedText, textItalic)
            ))
        }*//*
        bold.setOnLongClickListener {
            Toast.makeText(context, "Bold", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupItalic() {
        val italic = binding.italic
        italic.setOnClickListener {
            knife.italic(!knife.contains(KnifeText.FORMAT_ITALIC))
            *//*val start = knife.selectionStart
            val end = knife.selectionEnd
            val wholeText: String = knife.text.toString()
            val selectedText: String = knife.text.substring(start, end)
            val textItalic = "<i>$selectedText</i>"
            knife.setText(Html.fromHtml(wholeText.replace(selectedText, textItalic)))*//*
        }
        italic.setOnLongClickListener {
            Toast.makeText(context, "Italic", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupUnderline() {
        val underline = binding.underline
        underline.setOnClickListener {
            knife.underline(!knife.contains(KnifeText.FORMAT_UNDERLINED))
        }
        *//*val start = knife.selectionStart
        val end = knife.selectionEnd
        val wholeText: String = knife.text.toString()
        val selectedText: String = knife.text.substring(start, end)
        val textItalic = "<u>$selectedText</u>"
        knife.setText(Html.fromHtml(wholeText.replace(selectedText, textItalic)))*//*
        underline.setOnLongClickListener {
            Toast.makeText(context, "Underline", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupStrikethrough() {
        val strikethrough = binding.strikethrough
        strikethrough.setOnClickListener { knife.strikethrough(!knife.contains(KnifeText.FORMAT_STRIKETHROUGH)) }
        *//*strikethrough.setOnClickListener{
            val start = knife.selectionStart
            val end = knife.selectionEnd
            val wholeText: String = knife.text.toString()
            val selectedText: String = knife.text.substring(start, end)
            val textItalic = "<s>$selectedText</s>"
            knife.setText(Html.fromHtml(wholeText.replace(selectedText, textItalic)))
        }*//*
        strikethrough.setOnLongClickListener {
            Toast.makeText(context, "Strike Through", Toast.LENGTH_SHORT)
                .show()
            true
        }
    }

    private fun setupBullet() {
        val bullet = binding.bullet
        //bullet.setOnClickListener { knife.bullet(!knife.contains(KnifeText.FORMAT_BULLET)) }
        bullet.setOnClickListener{
            val start = knife.selectionStart
            val end = knife.selectionEnd
            val wholeText: Spannable = knife.getText()
            wholeText.setSpan(BulletSpan(), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            knife.setText(wholeText)
        }
        bullet.setOnLongClickListener {
            Toast.makeText(context, "Bullet", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupQuote() {
        val quote = binding.quote
        quote.setOnClickListener { knife.quote(!knife.contains(KnifeText.FORMAT_QUOTE)) }
        *//*val start = knife.selectionStart
        val end = knife.selectionEnd
        val wholeText: String = knife.text.toString()
        val selectedText: String = knife.text.substring(start, end)
        val textItalic = "<q>$selectedText</q>"
        knife.setText(Html.fromHtml(wholeText.replace(selectedText, textItalic)))
        *//*quote.setOnLongClickListener {
            Toast.makeText(context, "Quote", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupLink() {
        val link = binding.link
        link.setOnClickListener { showLinkDialog() }
        link.setOnLongClickListener {
            Toast.makeText(context, "Insert Link", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun showLinkDialog() {
        val start = knife.selectionStart
        val end = knife.selectionEnd
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyDialogTheme)
        builder.setCancelable(false)
        val view: View = layoutInflater.inflate(com.example.notesio.R.layout.dialog_link, null, false)
        val editText = view.findViewById<View>(com.example.notesio.R.id.edit) as EditText
        builder.setView(view)

        builder.setTitle("Insert Link")
        builder.setPositiveButton("OK",
            DialogInterface.OnClickListener { dialog, which ->
                val link = editText.text.toString().trim { it <= ' ' }
                if (TextUtils.isEmpty(link)) {
                    return@OnClickListener
                }

                // When KnifeText lose focus, use this method

                knife.movementMethod = LinkMovementMethod.getInstance()
                val spannable: Spannable = SpannableString(link)
                Linkify.addLinks(spannable, Linkify.WEB_URLS)
                val text: CharSequence = TextUtils.concat(spannable, "\u200B")
                knife.link(text.toString(), start, end)
                *//*val string = SpannableString("Text with a url span")
                string.setSpan(
                    URLSpan(link),
                    12,
                    15,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )*//*

            })
        builder.setNegativeButton("CANCEL",
            DialogInterface.OnClickListener { dialog, which ->
                // DO NOTHING HERE
            })
        builder.create().show()
    }


    private fun setupClear() {
        val clear = binding.clear
        clear.setOnClickListener { knife.clearFormats() }
        clear.setOnLongClickListener {
            Toast.makeText(context, "Format Clear", Toast.LENGTH_SHORT)
                .show()
            true
        }
    }*/

}