package co.il.jabrutouch.utils;

import androidx.fragment.app.Fragment;

/**
 * Created by israel kobler on 2/29/2016.
 */
public class AddFragment {

    /**
     * The enum Action.
     */
    public enum Action {
        /**
         * Add action.
         */
        ADD,
        /**
         * Replace action.
         */
        REPLACE,
        /**
         * Pop action.
         */
        POP,
        /**
         * Remove action.
         */
        REMOVE
    }

    private Fragment fragment;
    private int containerId ;
    private String tag ;
    private boolean popBackStack ;
    private String tagToBackStack ;
    private Action action ;

    /**
     * Instantiates a new Add fragment.
     */
    public AddFragment() {
    }

    /**
     * Instantiates a new Add fragment.
     *
     * @param fragment       the fragment
     * @param containerId    the container id
     * @param tag            the tag
     * @param popBackStack   the pop back stack
     * @param tagToBackStack the tag to back stack
     * @param action         the action
     */
    public AddFragment(Fragment fragment, int containerId, String tag, boolean popBackStack , String tagToBackStack, Action action) {
        this.fragment = fragment;
        this.containerId = containerId;
        this.popBackStack = popBackStack ;
        this.tag = tag;
        this.tagToBackStack = tagToBackStack;
        this.action = action;
    }

    /**
     * Instantiates a new Add fragment.
     *
     * @param fragment       the fragment
     * @param containerId    the container id
     * @param tag            the tag
     * @param tagToBackStack the tag to back stack
     * @param action         the action
     */
    public AddFragment(Fragment fragment, int containerId, String tag, String tagToBackStack, Action action) {
        this.fragment = fragment;
        this.containerId = containerId;
        this.tag = tag;
        this.tagToBackStack = tagToBackStack;
        this.action = action;
    }

    /**
     * Gets fragment.
     *
     * @return the fragment
     */
    public Fragment getFragment() {
        return fragment;
    }

    /**
     * Sets fragment.
     *
     * @param fragment the fragment
     */
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     * Gets container id.
     *
     * @return the container id
     */
    public int getContainerId() {
        return containerId;
    }

    /**
     * Sets container id.
     *
     * @param containerId the container id
     */
    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    /**
     * Gets tag.
     *
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets tag.
     *
     * @param tag the tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Pop back stack boolean.
     *
     * @return the boolean
     */
    public boolean popBackStack() {
        return popBackStack;
    }

    /**
     * Sets pop back stack.
     *
     * @param popBackStack the pop back stack
     */
    public void setPopBackStack(boolean popBackStack) {
        this.popBackStack = popBackStack;
    }

    /**
     * Gets tag to back stack.
     *
     * @return the tag to back stack
     */
    public String getTagToBackStack() {
        return tagToBackStack;
    }

    /**
     * Sets tag to back stack.
     *
     * @param tagToBackStack the tag to back stack
     */
    public void setTagToBackStack(String tagToBackStack) {
        this.tagToBackStack = tagToBackStack;
    }

    /**
     * Gets action.
     *
     * @return the action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Sets action.
     *
     * @param action the action
     */
    public void setAction(Action action) {
        this.action = action;
    }
}