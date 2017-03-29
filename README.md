# 100TopApps

Demo: https://youtu.be/_z1K1OXJEXg

Step 1:
Import Volley to get all datas:
  compile 'com.android.volley:volley:1.0.0'
  
Step 2:
Prepare RequestString and add to RequestQueue

Step 3:
Create item layout: layout_item.xml

Step 4:
Create an adapter for list view: ListViewAdapter

Step 5:
Use AsyncTask to downlaod all the images. Reference in ListViewAdapter

Step 6:
Build and task completed.


private void shareNews() {
        loadingRL.setVisibility(View.VISIBLE);
        if (isEdit) {
            wallPostModel = createWallPostModelByShareNews();
            wallDBAPI.uploadPost(wallPostModel, false).subscribe(new Action1() {
                @Override
                public void call(Object o) {
                    RxBus.get().post(BusConstants.UPDATE_POST, PostConvertUtility.postModelToPostItem(wallPostModel));
                    finish();
                }
            });
        } else {
            if (postItemData != null) {
                wallPostModel = createWallPostModelByShareNews();
                wallDBAPI.uploadPost(wallPostModel, false).subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        RxBus.get().post(BusConstants.UPDATE_POST, PostConvertUtility.postModelToPostItem(wallPostModel));
                        finish();
                    }
                });
            } else {
                Observable.just(newsItem.getNewsImageName())
                        .observeOn(Schedulers.io())
                        .map(new Func1<String, Bitmap>() {
                            @Override
                            public Bitmap call(String s) {
                                try {
                                    Bitmap bitmap = Picasso.with(context).load(newsItem.getNewsImageName()).get();
                                    return bitmap;
                                } catch (IOException e) {
                                    return null;
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Bitmap>() {
                            @Override
                            public void call(Bitmap b) {
                                if (b != null) {
                                    bitmap = b;
                                    wallPostModel = createWallPostModelByShareNews();
                                    uploadImage();
                                } else {
                                    Toast.makeText(context, "Sorry! Upload Image failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
