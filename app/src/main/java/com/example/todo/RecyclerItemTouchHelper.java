package com.example.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private Recadapter adapter;

    public RecyclerItemTouchHelper(Recadapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public  boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target){
        return false;
    }
    @Override
    public  void onSwiped(final RecyclerView.ViewHolder viewHolder,int direction){
        final int position=viewHolder.getAdapterPosition();
        if(direction==ItemTouchHelper.LEFT){
            AlertDialog.Builder builder=new AlertDialog.Builder(adapter.getcontext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this task?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                       adapter.deleteItem(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                     adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }
        else{
            adapter.editItem(position);
        }
    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dx, float dy, int actionState, boolean isCurrentlyActive){
        super.onChildDraw(c,recyclerView,viewHolder,dx,dy,actionState,isCurrentlyActive);
        Drawable icon;
        ColorDrawable background;
         View itemview=viewHolder.itemView;
         int backgroundCorneroffset=20;
         if(dx>0){
             icon= ContextCompat.getDrawable(adapter.getcontext(),R.drawable.ic_baseline_edit_24);
             background=new ColorDrawable(ContextCompat.getColor(adapter.getcontext(),R.color.colorPrimaryDark));
         }
         else{
             icon= ContextCompat.getDrawable(adapter.getcontext(),R.drawable.ic_baseline_delete_24);
             background=new ColorDrawable(Color.RED);
         }
         int iconmargin=(itemview.getHeight()-icon.getIntrinsicHeight())/2;
         int iconTop=itemview.getTop()+(itemview.getHeight()-icon.getIntrinsicHeight())/2;
         int iconBottom=iconTop+icon.getIntrinsicHeight();
         if(dx>0){//swiping to the right
             int iconLeft=itemview.getLeft()+iconmargin;
             int iconRight=itemview.getLeft()+iconmargin+icon.getIntrinsicHeight();
             icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);
             background.setBounds(itemview.getLeft(),itemview.getTop(),itemview.getLeft()+((int)dx)+backgroundCorneroffset,itemview.getBottom());
         }
         else if(dx<0){//swiping to the left
             int iconLeft=itemview.getRight()-iconmargin-icon.getIntrinsicHeight();
             int iconRight=itemview.getRight()-iconmargin;
             icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);
             background.setBounds(itemview.getRight()+((int)dx)-backgroundCorneroffset,itemview.getTop(),itemview.getRight(),itemview.getBottom());
         }
         else{
             background.setBounds(0,0,0,0);
         }
         background.draw(c);
         icon.draw(c);
    }
}
