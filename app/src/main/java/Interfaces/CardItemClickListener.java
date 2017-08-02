package Interfaces;

import android.view.View;

/**
 * Interface (s pripadajućom metodom)za pomoć kod pritisaka pojedinih elemenata liste/RecyclerViewa
 */

public interface CardItemClickListener {

     void itemClicked(View view, int position);
}
