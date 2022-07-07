package com.example.josephinemenge.pika.util;

/**
 * Created by bubbles on 6/13/17.
 */

public interface ItemTouchHelperAdapter {
        boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
