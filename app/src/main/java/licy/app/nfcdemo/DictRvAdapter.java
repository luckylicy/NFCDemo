package licy.app.nfcdemo;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import licy.app.nfcdemo.bean.DictBean;

/**
 * DictRvAdapter
 * description: TODO
 *
 * @author : Licy
 * @date : 2020/11/9
 * email ：licy3051@qq.com
 */
public class DictRvAdapter extends BaseQuickAdapter<DictBean.DataBean, BaseViewHolder> {
    public DictRvAdapter(int layoutResId) {
        super(layoutResId);
    }

    public DictRvAdapter(int layoutResId, @Nullable List<DictBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DictBean.DataBean dataBean) {
        baseViewHolder.setText(R.id.tv_key,dataBean.getId())
                .setText(R.id.tv_value,dataBean.getValue());
    }
}

    
    
       
    