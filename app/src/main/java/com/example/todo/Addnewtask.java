package com.example.todo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todo.Utils.Mydbhandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class Addnewtask extends BottomSheetDialogFragment {
    public static final String TAG="ActionBottomDialog";
    private EditText newtasktext;
    private Button newtasksavebtn;
    private Mydbhandler db;
    public static Addnewtask newInstance(){
        return new Addnewtask();
    }
    //BottomDialog box's oncreate
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);
    }
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup conteiner,@Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.new_task,conteiner,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        newtasktext= getView().findViewById(R.id.newtasktext);
        newtasksavebtn=getView().findViewById(R.id.btn);
        boolean isUpdate=false;
        final Bundle bundle=getArguments();
        if(bundle!=null){
            isUpdate=true;
            String task=bundle.getString("task");
            newtasktext.setText(task);
            if(task.length()>0)
                newtasksavebtn.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));

        }
        db=new Mydbhandler(getActivity());
        //db.openDatabase();
        newtasktext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                newtasksavebtn.setEnabled(true);
                if(s.toString().equals(""))
                    newtasksavebtn.setTextColor(Color.GRAY);
                else
                    newtasksavebtn.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        boolean finalIsUpdate = isUpdate;
        newtasksavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=newtasktext.getText().toString();
                if(finalIsUpdate)
                    db.updatetask(bundle.getInt("id"),text);
                else{
                    Model m=new Model();
                    m.setTask(text);
                    m.setStatus(0);
                    db.insertTask(m);
                }
                dismiss();
            }
        });
    }
    @Override
    public  void onDismiss(DialogInterface dialog){
        Activity activity=getActivity();
        if(activity instanceof DialogCloseListner){
            ((DialogCloseListner)activity).handleDialogClose(dialog);
        }
    }
}
