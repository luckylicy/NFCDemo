package licy.app.nfcdemo.bean;

import java.util.List;

/**
 * DictBean
 * description: TODO
 *
 * @author : Licy
 * @date : 2020/11/9
 * email ：licy3051@qq.com
 */
public class DictBean {


    /**
     * result : 1
     * message : null
     * data : [{"value":"离开现场时间","id":"levavescenetime"},{"value":"到达医院时间","id":"arrivehospitaltime"},{"value":"首次医疗接触时间","id":"fmctime"},{"value":"采血时间","id":"bloodcollectiontime"},{"value":"开始静脉溶栓时间","id":"thrombolyticstaticpushtime"},{"value":"初始药物:阿司匹林","id":"acsaspirintime"},{"value":"初始药物:氯呲格雷","id":"acschlorpyridintime"},{"value":"初始药物:替格瑞洛","id":"acstigrilotime"},{"value":"初始药物:术前抗凝","id":"acsanticoagulantmedicinetime"},{"value":"发病现场:静脉通路时间","id":"preemergencyvenouschanneltime"},{"value":"发病现场:气管插管时间","id":"preemergencytracheacannulatime"},{"value":"发病现场:心肺复苏时间","id":"preemergencycprtime"},{"value":"急诊现场:静脉通路时间","id":"inemergencyvenouschanneltime"},{"value":"急诊现场:气管插管时间","id":"inemergencytracheacannulatime"},{"value":"急诊现场:心肺复苏时间","id":"inemergencycprtime"},{"value":"胸痛 开始静脉溶栓时间","id":"afterthrombolysisbegintime"},{"value":"出车时间","id":"depart120time"},{"value":"到达现场时间","id":"arrivescenetime"}]
     */

    private int result;
    private String message;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * value : 离开现场时间
         * id : levavescenetime
         */

        private String value;
        private String id;

        public DataBean() {
        }

        public DataBean(String id,String value) {
            this.value = value;
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

    
    
       
    