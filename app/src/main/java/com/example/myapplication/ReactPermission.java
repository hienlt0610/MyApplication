package com.example.myapplication;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class ReactPermission {
    private AppCompatActivity activity;
    private OnPermissionGrant onPermissionGrant;
    private String[] permissions;

    public ReactPermission(AppCompatActivity activity) {
        this.activity = activity;
    }

    public static ReactPermission with(AppCompatActivity activity) {
        return new ReactPermission(activity);
    }

    public ReactPermission request(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public void execute(OnPermissionGrant onPermissionGrant) {
        FragmentManager fragmentManager = this.activity.getSupportFragmentManager();
        Fragment existFragment = fragmentManager.findFragmentByTag(RequestPermissionFragment.FRAGMENT_TAG);
        if (existFragment != null) {
            fragmentManager.beginTransaction().remove(existFragment).commit();
        }
        existFragment = RequestPermissionFragment.getInstance(this.permissions, onPermissionGrant);
        fragmentManager.beginTransaction().add(existFragment, null).commit();
    }


    public static class RequestPermissionFragment extends Fragment {
        public static String FRAGMENT_TAG = "request_permission_tag";
        private static int REQUEST_CODE = 123;
        private String[] permission;
        private OnPermissionGrant onPermissionGrant;

        public static RequestPermissionFragment getInstance(String[] permissions, OnPermissionGrant onPermissionGrant) {
            RequestPermissionFragment fragment = new RequestPermissionFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArray("permission", permissions);
            fragment.setArguments(bundle);
            fragment.onPermissionGrant = onPermissionGrant;
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() == null) return;
            this.permission = getArguments().getStringArray("permission");
            requestPermissions(this.permission, REQUEST_CODE);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            boolean isGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false;
                    break;
                }
            }
            if (isGranted && onPermissionGrant != null) {
                onPermissionGrant.onGranted();
            }
        }
    }

    public interface OnPermissionGrant{
        void onGranted();
    }
}
