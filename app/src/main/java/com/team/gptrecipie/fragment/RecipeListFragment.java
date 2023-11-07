package com.team.gptrecipie.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.team.gptrecipie.R;
import com.team.gptrecipie.SplashActivity;
import com.team.gptrecipie.activity.Login;
import com.team.gptrecipie.activity.RecipeActivity;
import com.team.gptrecipie.activity.RecipeListActivity;
import com.team.gptrecipie.activity.Signup;
import com.team.gptrecipie.dao.RecipeDAO;
import com.team.gptrecipie.model.Recipe;

import java.util.List;
import java.util.UUID;

 public class RecipeListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecipeAdaptor mRecipeAdaptor;
    private boolean mSubtitleVisible = false;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private Callbacks mCallBacks;


    public interface Callbacks {
        void onRecipeSelected(Recipe recipe);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallBacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_recipe_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences userSession = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        boolean isAuthenticated = userSession.getBoolean("isAuthenticated", false);

        final Intent intent;
        if (isAuthenticated == false) {
            intent = new Intent(getActivity(), Signup.class);
            startActivity(intent);
        }

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_recipe, menu);

        /*MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }*/
    }

    private void updateSubtitle() {

        int recipeCount = RecipeDAO.get(getActivity()).getRecipes().size();
        String subTitle = getResources().getQuantityString(R.plurals.subtitle_plural, recipeCount, recipeCount);

        if (!mSubtitleVisible) {
            subTitle = "";
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subTitle);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_recipe) {
            Recipe newRecipe = new Recipe();
            // TODO add recipe new logic
            RecipeDAO.get(getActivity()).addRecipe(newRecipe);
            updateUI();
            mCallBacks.onRecipeSelected(newRecipe);
            /*Intent crimePagerintent = CrimePagerActivity.newCrimePagerActivityIntent(getActivity(), newCrime.getId());
            startActivity(crimePagerintent);*/
            return true;
        } else if (item.getItemId() == R.id.show_subtitle) {
            SharedPreferences userSession = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = userSession.edit();

            Log.i("Checking session", userSession.getAll().toString());
            editor.putBoolean("isAuthenticated", false);
            editor.remove("email");
            editor.apply();
            Log.i("Checking session", userSession.getAll().toString());

            final Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);

            getActivity().finish();
            /*
            mSubtitleVisible = !mSubtitleVisible;
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        SharedPreferences userSession = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        boolean isAuthenticated = userSession.getBoolean("isAuthenticated", false);

        final Intent intent;
        if (isAuthenticated == false) {
            intent = new Intent(getActivity(), Signup.class);
            startActivity(intent);
        }*/
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RecipeActivity.REQUEST_CRIME) {
            UUID uuid = (UUID) data.getSerializableExtra(RecipeActivity.EXTRA_RECIPE_ID);
            mRecipeAdaptor.notifyItemChanged(uuid);
        }

    }

    public void updateUI() {
        RecipeDAO recipeDAO = RecipeDAO.get(getActivity());
        List<Recipe> recipes = recipeDAO.getRecipes();
        if (mRecipeAdaptor == null) {
            mRecipeAdaptor = new RecipeAdaptor(recipes);
            mRecyclerView.setAdapter(mRecipeAdaptor);
        } else {
            mRecipeAdaptor.setCrimes(recipes);
            mRecipeAdaptor.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mDeliciousImageView;
        private Recipe mRecipe;

        public RecipeHolder(LayoutInflater inflater, ViewGroup itemView) {
            super(inflater.inflate(R.layout.item_recipe_list, itemView, false));
            // View listItemView = inflater.inflate(R.layout.item_crime_list, itemView, false);

            mTitleTextView = itemView.findViewById(R.id.recipe_title_list);
            mDateTextView = itemView.findViewById(R.id.recipe_date_list);
            mDeliciousImageView = itemView.findViewById(R.id.item_recipe_delicious);
        }


        public RecipeHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.recipe_title_list);
            mDateTextView = itemView.findViewById(R.id.recipe_date_list);
            mDeliciousImageView = itemView.findViewById(R.id.item_recipe_delicious);
        }

        public void bind(Recipe recipe) {
            mRecipe = recipe;
            mTitleTextView.setText(recipe.getTitle());
            mDateTextView.setText(DateFormat.format("yyyy/MM/dd HH:mm:ss", recipe.getDate()).toString());
            mDeliciousImageView.setVisibility(recipe.isDelicious() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            /*Toast.makeText(getActivity(),
                            mCrime.getTitle() + " cliecked!", Toast.LENGTH_LONG)
                    .show();*/
            /*Intent intent = CrimePagerActivity.newCrimePagerActivityIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent, CrimePagerActivity.REQUEST_CRIME);*/
            mCallBacks.onRecipeSelected(mRecipe);
        }

    }

    private class RecipeAdaptor extends RecyclerView.Adapter<RecipeHolder>  {
        List<Recipe> mRecipes;

        public RecipeAdaptor(List<Recipe> recipes) {
            this.mRecipes = recipes;
        }

        public void setCrimes(List<Recipe> recipes) {
            mRecipes = recipes;
        }

        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_recipe_list, parent, false);
            int[] a = new int[0];
            return new RecipeHolder(view);
        }


        @Override
        public int getItemCount() {
            return mRecipes.size();
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
            Recipe recipe = mRecipes.get(position);

            holder.bind(recipe);
        }

        public void notifyItemChanged(UUID uuid) {
            int position = -1;
            for (int i = 0; i < mRecipes.size(); i++) {
                if (mRecipes.get(i).getId().equals(uuid)) {
                    position = i;
                }
            }

            if (position == -1) {
                notifyDataSetChanged();
            } else {
                notifyItemChanged(position);
            }
        }
    }
}
