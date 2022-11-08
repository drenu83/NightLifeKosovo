package com.example.nightlifekosovo.Helpers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentProcess {
    public static void setCurrentFragment(FragmentManager fragmentManager, Fragment fragment, int containerId) {
        fragmentManager.beginTransaction().replace(containerId, fragment).addToBackStack("").commit();
    }

}
