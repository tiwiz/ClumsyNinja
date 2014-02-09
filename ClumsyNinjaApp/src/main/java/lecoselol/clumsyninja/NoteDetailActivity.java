package lecoselol.clumsyninja;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * An activity representing a single Note detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link NoteListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link NoteDetailFragment}.
 */
public class NoteDetailActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {


    private NoteDetailFragment mDetailFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(NoteDetailFragment.ARG_ITEM_ID,
                                getIntent().getIntExtra(NoteDetailFragment.ARG_ITEM_ID, -1));
            NoteDetailFragment fragment = new NoteDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                                .add(R.id.note_detail_container, fragment, "detail")
                                .commit();
        }

        if (isBeamAvailable()) {
            //gets NFC Adapter
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            // Register callback to set NDEF message
            nfcAdapter.setNdefPushMessageCallback(this, this);
            // Register callback to listen for message-sent success
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(this, new Intent(this, NoteListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mDetailFrag = (NoteDetailFragment) getFragmentManager().findFragmentByTag("detail");
    }

    @Override
    protected void onPause() {
        super.onPause();

        mDetailFrag = null;
    }

    private void navigateUpTo(Activity activity, Intent upIntent) {
        upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(upIntent);
        activity.finish();
    }

    private final boolean isBeamAvailable(){

        final PackageManager packageManager = getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_NFC);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        // This beams a URL
        NdefRecord urlRecord = NdefRecord.createUri(mDetailFrag.getItem().getShareableString());
        NdefRecord[] urlVectRecord = new NdefRecord[]{urlRecord};
        NdefMessage ndefMessage = new NdefMessage(urlVectRecord);
        return ndefMessage;
    }

    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {
    }
}
