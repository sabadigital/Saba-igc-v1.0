package com.saba.igc.org.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.saba.igc.org.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeeklyProgramsTabsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeeklyProgramsTabsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//public class WeeklyProgramsTabsFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment WeeklyProgramsTabsFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static WeeklyProgramsTabsFragment newInstance(String param1, String param2) {
//        WeeklyProgramsTabsFragment fragment = new WeeklyProgramsTabsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public WeeklyProgramsTabsFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_weekly_programs_tabs, container, false);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }
//
//}

public class WeeklyProgramsTabsFragment extends Fragment {

    TabHost tHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weekly_programs_tabs, container, false);

        tHost = (TabHost) view.findViewById(android.R.id.tabhost);
        tHost.setup();

        return view;
    }

//    public static class MyPagerAdapter extends FragmentPagerAdapter {
//        private static int NUM_ITEMS = 3;
//
//        public MyPagerAdapter(FragmentManager fragmentManager) {
//            super(fragmentManager);
//        }

//        // Returns total number of pages
//        @Override
//        public int getCount() {
//            return NUM_ITEMS;
//        }
//
//        // Returns the fragment to display for that page
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 0: // Fragment # 0 - This will show FirstFragment
//                    return FirstFragment.newInstance(0, "Page # 1");
//                case 1: // Fragment # 0 - This will show FirstFragment different title
//                    return FirstFragment.newInstance(1, "Page # 2");
//                case 2: // Fragment # 1 - This will show SecondFragment
//                    return SecondFragment.newInstance(2, "Page # 3");
//                default:
//                    return null;
//            }
//        }
//
//        // Returns the page title for the top indicator
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "Page " + position;
//        }
//
//    }
}