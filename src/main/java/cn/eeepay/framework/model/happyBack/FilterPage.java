package cn.eeepay.framework.model.happyBack;

/**
 * Created by Administrator on 2018/12/27/027.
 * @author  liuks
 * 欢乐返统计分页
 */
public class FilterPage {

    private int startPage;//分页开始
    private int length;//每页大小
    private int page;//当前第几页

    /**
     * 初始化分页
     */
    public FilterPage() {
        this.length = 10000;
        this.page = 1;
        initPage();
    }

    /**
     * 设置页数
     */
    public void setPage(int page) {
        this.page = page;
        initPage();
    }

    public int getPage() {
        return page;
    }

    private void initPage(){
        if(page>=1&&length>0){
            startPage=length*(page-1);
        }
    }


    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
