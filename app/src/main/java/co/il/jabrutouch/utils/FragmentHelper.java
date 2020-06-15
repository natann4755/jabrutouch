package co.il.jabrutouch.utils;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by israel kobler on 2/29/2016.
 * Class responsible to manage the app's fragments.
 */
public class FragmentHelper {

    private final FragmentActivity mFragmentActivity;
    private List<AddFragment> commitFragments = new ArrayList<>();
    private ActivityRunning mActivityRunning ;

    /**
     * Class constructor.
     *
     * @param fragmentActivity the fragment activity
     * @param activityRunning  the activity running
     */
    public FragmentHelper(FragmentActivity fragmentActivity , ActivityRunning activityRunning) {

        this.mFragmentActivity = fragmentActivity ;
        this.mActivityRunning = activityRunning ;
    }

    /**
     * Add a new fragment.
     *
     * @param containerId    the container id
     * @param fragment       the fragment
     * @param tag            the tag
     * @param tagToBackStack the tag to back stack
     */
    public void addFragment(int containerId, Fragment fragment, String tag, String tagToBackStack ) {

        if (mActivityRunning.isRunning()) {

            FragmentTransaction fragmentTransaction = this.mFragmentActivity.getSupportFragmentManager().beginTransaction();

            if (tagToBackStack != null) {

                fragmentTransaction.addToBackStack(tagToBackStack);
            }

            fragmentTransaction.add(containerId, fragment, tag);
            fragmentTransaction.commit();

        } else {

            addToListCommit(containerId, fragment, tag, tagToBackStack, AddFragment.Action.ADD);
        }
    }

    /**
     * Add a new fragment.
     *
     * @param containerId the container id
     * @param fragment    the fragment
     * @param tag         the tag
     */
    public void addFragment(int containerId, Fragment fragment, String tag) {

        if (mActivityRunning.isRunning()) {

            FragmentTransaction fragmentTransaction = this.mFragmentActivity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(containerId, fragment, tag);
            fragmentTransaction.commit();

        } else {

            addToListCommit(containerId, fragment, tag, null, AddFragment.Action.ADD);
        }
    }

    /**
     * Replace fragment.
     *
     * @param containerId    the container id
     * @param fragment       the fragment
     * @param tag            the tag
     * @param tagToBackStack the tag to back stack
     */
    public void replaceFragment(int containerId, Fragment fragment, String tag, String tagToBackStack) {

        if(mActivityRunning.isRunning()) {

            FragmentTransaction fragmentTransaction = this.mFragmentActivity.getSupportFragmentManager().beginTransaction();

            if (tagToBackStack != null) {

                fragmentTransaction.addToBackStack(tagToBackStack);
            }

            fragmentTransaction.replace(containerId, fragment, tag);

            fragmentTransaction.commit();

        } else {

            addToListCommit(containerId, fragment, tag, tagToBackStack, AddFragment.Action.REPLACE);
        }
    }


    /**
     * Pop back stack.
     *
     * @param name the community
     */
    public void popBackStack(String name) {

        if (mActivityRunning.isRunning())

            mFragmentActivity.getSupportFragmentManager().popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        else {

            addToListCommit(0, null, name, null, AddFragment.Action.POP);
        }
    }

    /**
     * Remove all fragments.
     */
    public void removeAllFragments(){

        @SuppressLint("RestrictedApi") List<Fragment> fragments = mFragmentActivity.getSupportFragmentManager().getFragments();
        mFragmentActivity.getSupportFragmentManager().popBackStack();
        mFragmentActivity.getSupportFragmentManager().popBackStack(null , FragmentManager.POP_BACK_STACK_INCLUSIVE );
        //mFragmentActivity.getSupportFragmentManager().popBackStack(R.id.FL_dashboard , FragmentManager.POP_BACK_STACK_INCLUSIVE );
        // popBackStack(int id, int flags) receive a state id, not a container id like in the line above.

        if (fragments != null) {

            for (Fragment fragment : fragments) {
                removeFragment(fragment);
            }
        }
    }

    /**
     * Remove a single fragment.
     * This function is used by other classes.
     *
     * @param tag          the tag
     * @param popBackStack the pop back stack
     */
    public void removeFragment(String tag , boolean popBackStack) {

        if (mActivityRunning.isRunning()) {

            Fragment fragment = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
            removeFragment(fragment);

            if (popBackStack){

                mFragmentActivity.getSupportFragmentManager().popBackStack();
            }

        } else {

            addToListCommit(0,null,tag,popBackStack ,null, AddFragment.Action.REMOVE);
        }
    }

    /**
     * Remove a single fragment.
     * This function is only used by this class.
     * @param fragment
     */
    public void removeFragment(Fragment fragment) {

        if (fragment != null) {

            FragmentTransaction fragmentTransaction = this.mFragmentActivity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment)
                    .commit();
        }
    }

    /**
     * Commit all.
     */
    public void commitAll() {

        for (AddFragment fragment : commitFragments) {

            if (fragment.getAction() == AddFragment.Action.ADD) {

                addFragment(fragment.getContainerId() , fragment.getFragment() , fragment.getTag());

            } else if (fragment.getAction() == AddFragment.Action.REPLACE) {

                replaceFragment(fragment.getContainerId(),fragment.getFragment(),fragment.getTag(),fragment.getTagToBackStack());

            } else if(fragment.getAction() == AddFragment.Action.POP) {

                popBackStack(fragment.getTag());

            } else if (fragment.getAction() == AddFragment.Action.REMOVE) {

                removeFragment(fragment.getTag(),fragment.popBackStack());
            }
        }

        commitFragments.clear();
    }


    private void addToListCommit(int containerId, Fragment fragment, String tag, boolean popBackStack , String tagToBackStack, AddFragment.Action action) {

        AddFragment addFragment = new AddFragment(
                fragment,
                containerId,
                tag,
                popBackStack ,
                tagToBackStack,
                action);

        commitFragments.add(addFragment);
    }

    private void addToListCommit(int containerId, Fragment fragment, String tag , String tagToBackStack, AddFragment.Action action) {

        AddFragment addFragment = new AddFragment(
                fragment,
                containerId,
                tag,
                tagToBackStack,
                action);

        commitFragments.add(addFragment);
    }

    /**
     * Get support fragment manager android . support . v 4 . app . fragment manager.
     *
     * @return the android . support . v 4 . app . fragment manager
     */
    public androidx.fragment.app.FragmentManager getSupportFragmentManager() {

        return mFragmentActivity.getSupportFragmentManager();
    }

    /**
     * Remove from back stack.
     *
     * @param tag the tag
     */
    public void removeFromBackStack(String... tag) {

        for(String t :  tag) {
            mFragmentActivity.getSupportFragmentManager().popBackStack(t , 0);
        }
    }

    /**
     * Is current boolean.
     *
     * @param tag the tag
     * @return the boolean
     */
    @SuppressLint("RestrictedApi")
    public boolean isCurrent(String tag) {

        if (mFragmentActivity != null && mFragmentActivity.getSupportFragmentManager() != null &&
                mFragmentActivity.getSupportFragmentManager().getFragments() != null) {

            int size = mFragmentActivity.getSupportFragmentManager().getFragments().size();

            if (size > 0) {

                Fragment fragment =
                        mFragmentActivity.getSupportFragmentManager().getFragments().get(size - 1);

                return fragment != null && fragment.getTag() != null &&
                        fragment.getTag().equals(tag);
            }
        }

        return false;
    }
}