package cs6018.lifestyleapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StatsFrag extends Fragment {

//    private TextView mTitle;
//
//
//    public StatsFrag() {
//        // Required empty public constructor
//    }
//
//    /**
//     * unpack the user profile data.
//     * @param savedInstanceState
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    /**
//     * place the user profile data in the appropriate components.
//     * @param inflater
//     * @param container
//     * @param savedInstanceState
//     * @return
//     */
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        View view = inflater.inflate(R.layout.fragment_stats, container, false);
//
//        Bundle bundle = this.getArguments();
//
//        //Get the validated user names
//
//        return view;
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }

    public TextView countTv;
    public Button countBtn;

    public StatsFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        countTv = (TextView) view.findViewById(R.id.count_tv);
        countTv.setText("0");
        countBtn = (Button) view.findViewById(R.id.count_btn);
        countBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseCount();
            }
        });
        return view;
    }

    private void increaseCount() {
        int current = Integer.parseInt((String) countTv.getText());
        countTv.setText(String.valueOf(current+1));
    }

}
