package com.team.gptrecipie.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.team.gptrecipie.R;
import com.team.gptrecipie.activity.RecipeActivity;
import com.team.gptrecipie.dao.RecipeDAO;
import com.team.gptrecipie.model.Recipe;
import com.team.gptrecipie.service.GPTGenerateService;
import com.team.gptrecipie.utils.PictureUtils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RecipeFragment extends Fragment {

    private Recipe mRecipe;
    private EditText mTitleText;
    private EditText mIngredientText;
    private TextView mDetailText;
    private Button mDateButton;
    private CheckBox mDeliciousCheckBox;
    private Button mShareButton;
    private Button mGenerateButton;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;
    private File mPhotoFile;
    private GPTGenerateService mService;

    private static final String ARG_RECIPE_ID = "crime_id";
    private static final int REQUEST_DATE_PICKER = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;
    private CallBacks mCallBacks;

    public interface CallBacks {
        public void onRecipeUpdated(Recipe recipe);
    }

    public static RecipeFragment newInstance(UUID uuid) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE_ID, uuid);
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setArguments(args);
        return recipeFragment;
    }

    private RecipeFragment() {
    }

    private String getRecipeSummary() {
        // TODO change report
        String deliciousString = null;
        if (mRecipe.isDelicious()) {
            deliciousString = getString(R.string.recipe_summary_delicious);
        } else {
            deliciousString = getString(R.string.recipe_summary_not_delicious);
        }
        String dateString = DateFormat.format("yyyy/MM/dd HH:mm:ss", mRecipe.getDate()).toString();

        String ingredientString = mRecipe.getIngredient();
        if (ingredientString == null) {
            ingredientString = getString(R.string.recipe_summary_no_ingredient);
        } else {
            ingredientString = getString(R.string.recipe_summary_ingredient, ingredientString);
        }

        String gptContentString = mRecipe.getContent();
        if (gptContentString == null) {
            gptContentString = getString(R.string.recipe_summary_no_content);
        } else {
            gptContentString = getString(R.string.recipe_summary_content, gptContentString);
        }

        String report = getString(R.string.recipe_summary, mRecipe.getTitle(), dateString, deliciousString, ingredientString, gptContentString);
        return report;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallBacks = (CallBacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mService = new GPTGenerateService();
        UUID uuid = (UUID) getArguments().getSerializable(ARG_RECIPE_ID);
        mRecipe = RecipeDAO.get(getActivity()).getRecipe(uuid);
        mPhotoFile = RecipeDAO.get(getActivity()).getPhotoFile(mRecipe);
    }

    @Override
    public void onPause() {
        super.onPause();

        updateRecipe();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Intent data = new Intent();
        data.putExtra(RecipeActivity.EXTRA_RECIPE_ID, mRecipe.getId());
        getActivity().setResult(RecipeActivity.REQUEST_CRIME, data);
    }

    private static final String DIALOG_DATE = "DialogDate";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);

        mDateButton = v.findViewById(R.id.recipe_date);
        updateDate();
        // mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mRecipe.getDate());
                dialog.setTargetFragment(RecipeFragment.this, REQUEST_DATE_PICKER);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mDetailText = v.findViewById(R.id.recipe_detail);
        mDetailText.setText(getRecipeSummary());

        mDeliciousCheckBox = v.findViewById(R.id.recipe_delicious);
        mDeliciousCheckBox.setChecked(mRecipe.isDelicious());
        mDeliciousCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mRecipe.setDelicious(b);
                updateRecipe();
            }
        });

        mTitleText = v.findViewById(R.id.recipe_title);
        mTitleText.setText(mRecipe.getTitle());
        mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRecipe.setTitle(charSequence.toString());
                updateRecipe();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateRecipe();
            }
        });

        mIngredientText = v.findViewById(R.id.recipe_ingredient);
        mIngredientText.setText(mRecipe.getIngredient());

        mIngredientText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // User has finished inputting text
                    String userInput = mIngredientText.getText().toString();
                    mRecipe.setIngredient(userInput);
                    updateRecipe();
                    return true; // Consume the event
                }

                return false;
            }
        });
        mIngredientText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRecipe.setIngredient(charSequence.toString());
                updateRecipe();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateRecipe();
            }
        });



        mShareButton = v.findViewById(R.id.recipe_summary);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getRecipeSummary());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));

                Intent.createChooser(intent, getString(R.string.share_recipe));
                startActivity(intent);
            }
        });


        mGenerateButton = v.findViewById(R.id.recipe_regenerate);
        mGenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // intent.addCategory(Intent.CATEGORY_HOME);
                if (mRecipe.getIngredient() == null) {
                    mGenerateButton.setEnabled(false);
                } else {
                    String content = mService.generateContent(mRecipe);
                    if (content != null) {
                        mRecipe.setContent(content);
                        updateRecipe();
                    }
                }
            }
        });
        if (mRecipe.getContent() != null) {
            mGenerateButton.setText(mRecipe.getContent());
        }

        mPhotoButton = v.findViewById(R.id.recipe_camera);
        Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakeImage = (mPhotoFile != null)
                /*&& (captureImage.resolveActivity(getActivity().getPackageManager()) != null)*/;
        mPhotoButton.setEnabled(canTakeImage);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.team.gptrecipie.fileprovider",
                        mPhotoFile
                );

                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    // assign the right to write image
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);

            }
        });


        mPhotoView = v.findViewById(R.id.recipe_photo);
        updatePhotoView();
        return v;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE_PICKER) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mRecipe.setDate(date);
            updateRecipe();
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri uri = data.getData();
            String[] queryField = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor cursor = getActivity().getContentResolver().query(uri, queryField, null, null, null);

            try {
                if (cursor.getCount() == 0) {
                    return;
                }

                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                mRecipe.setContent(suspect);
                updateRecipe();
                mGenerateButton.setText(suspect);
            } finally {
                cursor.close();
            }

        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.leeplay.internetcrime.fileprovider",
                    mPhotoFile
            );

            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateRecipe();
            updatePhotoView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("yyyy/MM/dd HH:mm:ss", mRecipe.getDate()).toString());
    }

    private void updateRecipe() {
        RecipeDAO.get(getActivity())
                .updateRecipe(mRecipe);
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScalesBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }

    }
}
