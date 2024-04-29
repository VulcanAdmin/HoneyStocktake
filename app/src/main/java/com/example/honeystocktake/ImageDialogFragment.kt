package com.example.honeystocktake

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val imagePath = arguments?.getString("imagePath")

        // Create a dialog builder
        val builder = AlertDialog.Builder(requireActivity())
        //builder.setTitle("Enlarged Image")

        // Inflate a custom layout for the dialog
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_image, null)
        builder.setView(dialogView)

        // Load the enlarged image into an ImageView in the dialog layout
        val photoView = dialogView.findViewById<PhotoView>(R.id.dialogPhotoView)

        //online image
        Picasso.get()
            .load(imagePath)
            .placeholder(R.drawable.loading)
            .error(R.drawable.logoshort_removebg_preview)
            .into(photoView)

        //on server image
        //Glide.with(requireContext()).load(imagePath).into(imageView)

        // Create and return the dialog
        return builder.create()
    }

    companion object {
        fun newInstance(imagePath: String): ImageDialogFragment {
            val fragment = ImageDialogFragment()
            val args = Bundle()
            args.putString("imagePath", imagePath)
            fragment.arguments = args
            return fragment
        }
    }
}