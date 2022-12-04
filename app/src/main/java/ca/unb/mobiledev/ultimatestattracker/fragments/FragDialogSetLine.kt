import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import ca.unb.mobiledev.ultimatestattracker.model.Player
import ca.unb.mobiledev.ultimatestattracker.model.Team
import java.util.*

class FragDialogSetLine(val team : Team) : DialogFragment() {

    var positiveButton : Button? = null
    internal lateinit var listener: DialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val playerArray = Array<String>(team.players.size) { i -> team.players[i].getFormattedName() }
        val selectedPlayers = ArrayList<Player>()
        var count = 0
        Log.d("FragDialogSetLine", "playerArray: ${playerArray.contentToString()}")

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Select Lineup")
                .setPositiveButton("Confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogPositiveClick(this, selectedPlayers)
                    })
                .setMultiChoiceItems(playerArray, null,
                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                        count += if (isChecked) 1 else -1
                        if(isChecked) {
                            selectedPlayers.add(team.players[which])
                        } else {
                            selectedPlayers.remove(team.players[which])
                        }

                        if(count > 7) {
                            Toast.makeText(context, "You can only select 7 players", Toast.LENGTH_SHORT).show()
                            (dialog as AlertDialog).listView.setItemChecked(which, false)
                            selectedPlayers.remove(team.players[which])
                            count--
                            positiveButton?.isEnabled = false
                        }
                        if(count < 7) {
                            positiveButton?.isEnabled = false
                        }
                        if(count == 7) {
                            positiveButton?.isEnabled = true
                        }
                    })
            // Create the AlertDialog object and return it
            builder.create() as AlertDialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        val d = dialog as AlertDialog?
        if (d != null) {
            positiveButton = d.getButton(Dialog.BUTTON_POSITIVE)
            positiveButton?.isEnabled = false
        }
    }

    interface DialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, selectedPlayers: ArrayList<Player>)
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as DialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }
}