package nz.ac.aucklanduni.se306project1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.HorizontalScrollView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import nz.ac.aucklanduni.se306project1.R;
import nz.ac.aucklanduni.se306project1.adapters.ListRecyclerAdapter;
import nz.ac.aucklanduni.se306project1.data.Constants;
import nz.ac.aucklanduni.se306project1.databinding.ActivityListBinding;
import nz.ac.aucklanduni.se306project1.databinding.SubcategoryChipBinding;
import nz.ac.aucklanduni.se306project1.iconbuttons.BackButton;
import nz.ac.aucklanduni.se306project1.itemdecorations.GridSpacingItemDecoration;
import nz.ac.aucklanduni.se306project1.models.enums.Category;
import nz.ac.aucklanduni.se306project1.models.items.Item;
import nz.ac.aucklanduni.se306project1.viewholders.ItemCardViewHolder;
import nz.ac.aucklanduni.se306project1.viewmodels.ItemSearchViewModel;
import nz.ac.aucklanduni.se306project1.viewmodels.ListViewModel;

public class ListActivity extends TopBarActivity {

    private ActivityListBinding binding;
    private ItemSearchViewModel searchViewModel;
    private ListViewModel listViewModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityListBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.binding.getRoot());

        final Category category = Category.fromId(this.getIntent().getStringExtra(Constants.IntentKeys.CATEGORY_ID));

        this.listViewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(ListViewModel.initializer))
                .get(ListViewModel.class);
        this.searchViewModel = new ViewModelProvider(this).get(ItemSearchViewModel.class);

        this.listViewModel.getItemDataProvider().getItemsByCategory(category)
                .thenAccept(this.searchViewModel::setOriginalItems);

        final RecyclerView recyclerView = this.binding.listRecyclerView;

        final ListRecyclerAdapter<Item, ?> adapter = new ListRecyclerAdapter<>(
                this, this.searchViewModel.getFilteredItems(), ItemCardViewHolder.Builder.INSTANCE);

        recyclerView.setAdapter(adapter);
        GridSpacingItemDecoration.attachGrid(recyclerView, this, 2, 12, 20);

        this.topBarViewModel.setStartIconButton(new BackButton(new Intent(this, HomeActivity.class)));
        this.topBarViewModel.setTitle(category.getDisplayName(this.getResources()));
        this.binding.categoryImage.setImageResource(category.getCategoryImageId());

        this.addCategorySpecificUI();

    }

    private void addCategorySpecificUI() {
        final String[] labels = new String[]{"Personal Protective Equipment", "Clothing", "Vehicles", "Tools"};

        final HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        final ChipGroup chipGroup = new ChipGroup(this);
        final int horizontalPadding = this.getResources().getDimensionPixelSize(R.dimen.top_bar_horizontal_padding);

        horizontalScrollView.addView(chipGroup);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        chipGroup.setSingleLine(true);
        chipGroup.setPadding(horizontalPadding, 0, horizontalPadding, 0);

        for (final String label : labels) {
            final Chip chip = SubcategoryChipBinding.inflate(this.getLayoutInflater()).getRoot();
            chip.setText(label);
            chip.setOnCheckedChangeListener((button, isChecked) -> {
                System.out.println("hi :)");
            });
            
            chipGroup.addView(chip);
        }

        this.binding.topBarContainer.addView(horizontalScrollView);
    }
}