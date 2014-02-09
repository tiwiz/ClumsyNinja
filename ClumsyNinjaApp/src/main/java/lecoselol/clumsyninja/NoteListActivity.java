package lecoselol.clumsyninja;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * An activity representing a list of Notes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NoteDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link NoteListFragment} and the item details
 * (if present) is a {@link NoteDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link NoteListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class NoteListActivity extends Activity
    implements NoteListFragment.Callbacks {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(false);

        if (findViewById(R.id.note_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((NoteListFragment) getFragmentManager()
                .findFragmentById(R.id.note_list))
                .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
            {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                lockStuffUp();
            }
            return true;

            case R.id.action_add:
            {
                aggiungiUnaRobaAllaListaDelleCose();
            }
            break;

            case R.id.action_about:
            {
                startActivity(new Intent(this, AboutActivity.class));
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void lockStuffUp() {
        // TODO relock your shit
    }

    private void aggiungiUnaRobaAllaListaDelleCose() {
        onItemSelected(null);
    }

    /**
     * Callback method from {@link NoteListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            int idNum = id != null ? Integer.parseInt(id) : -1;
            arguments.putInt(NoteDetailFragment.ARG_ITEM_ID, idNum);
            arguments.putBoolean(NoteDetailFragment.ARG_SHOWMENU, true);
            NoteDetailFragment fragment = new NoteDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                                .replace(R.id.note_detail_container, fragment, "detail")
                                .commit();

        }
        else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, NoteDetailActivity.class);
            int idNum = id != null ? Integer.parseInt(id) : -1;
            detailIntent.putExtra(NoteDetailFragment.ARG_ITEM_ID, idNum);
            startActivity(detailIntent);
        }
    }

    public void refreshList() {
        final NoteListFragment listFrag = (NoteListFragment) getFragmentManager().findFragmentById(R.id.note_list);
        listFrag.refreshList();
    }
}
