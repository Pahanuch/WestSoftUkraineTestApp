package ua.it.pavlo.tykhonov.westsoftukrainetestapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.it.pavlo.tykhonov.westsoftukrainetestapp.R;
import ua.it.pavlo.tykhonov.westsoftukrainetestapp.content.User;


/**
 * Created by Tikho on 11.11.2016.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    String LOG_TAG = UsersAdapter.class.getSimpleName();

    private Context context;
    private List<User> usersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id) TextView id;
        @BindView(R.id.firstName) TextView firstName;
        @BindView(R.id.lastName) TextView lastName;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public UsersAdapter(Context context, List<User> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = usersList.get(position);

        int id = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        holder.id.setText(String.valueOf(id));
        holder.firstName.setText(firstName);
        holder.lastName.setText(lastName);

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
