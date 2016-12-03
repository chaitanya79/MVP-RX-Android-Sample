package com.task.ui.component.ScooterLocation;

import android.widget.ImageView;

import com.task.App;
import com.task.R;
import com.task.ui.base.BaseActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by AhmedEltaher on 25/11/16.
 */

public class ProductDetailsActivity extends BaseActivity implements ProductDetailsView {
    @Inject
    ProductDetailsPresenter presenter;
    @Bind(R.id.iv_motorcycle_image)
    ImageView ivProductImage;

    @Override
    protected void initializeDagger() {
        App app = (App) getApplicationContext();
        app.getMainComponent().inject(ProductDetailsActivity.this);
    }

    @Override
    protected void initializePresenter() {
        super.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.product_details_activity;
    }

    @Override
    public void openSettingsView() {

    }

    @Override
    public void openHomeView() {

    }

    @Override
    public void initializeProductFullImage(String imageURL) {
        Picasso.with(ivProductImage.getContext()).load(imageURL).placeholder(R.drawable
            .shirt_icon).fit().into(ivProductImage);
    }
}