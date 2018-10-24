package cs6018.lifestyleapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs6018.lifestyleapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToolsFrag extends Fragment {

    public ToolsFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tools, container, false);

        return view;
    }

}
