package cn.buaa.myweixin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.StaticLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


/**
 * 
 * @author geniuseoe2012
 *  更多精彩，请关注我的CSDN博客http://blog.csdn.net/geniuseoe2012
 *  android开发交流群：200102476
 */
public class ChatActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */

	private Button mBtnSend;
	private Button mBtnBack;
	private EditText mEditTextContent;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	private static int flag=0;
    /*
     * insert SQL
     * author:ZhangLong
     * time:2014\5\6
     */
	private static int miscount = 0;
	private SQLiteDatabase mSQLDatabase = null;
	
	private final static String DATABASE_NAME = "MIMI.db";
	private final static String TABLE_NAME = "table1";
	
	private final static String TABLE_ID = "id";
	private final static String TABLE_TIME = "time";
	private final static String TABLE_WHO = "who";
	private final static String TABLE_WORD = "word";
	
	
	private final static String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS table1 ( id INTERGER PRIMARY KEY , time TEXT , who INTERGER , word TEXT ) ";
											
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //SQL
        mSQLDatabase = this.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        
        
        	//创建表
     
        	mSQLDatabase.execSQL(CREATE_TABLE);
        	//插入初始项
        	if(flag==0){
        	mSQLDatabase.execSQL( " INSERT INTO table1 ( id , time , who , word ) values ( 0 , '2012-09-01 18:00' , 0 , '有大吗' ) " );
        	mSQLDatabase.execSQL( " INSERT INTO table1 ( id , time , who , word ) values ( 1 , '2012-09-01 18:10' , 1 , '有！你呢' ) " );
        	flag=1;
        	}

        	setContentView(R.layout.chat_xiaohei);
            //启动activity时不自动弹出软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
            initView();
        initData();
    }
    
	public int id ;public int who ;
	public String time = null;public String word = null;
	public String[]msgArray=null;
	public String[]dataArray =null;
	
	
   
    //添加插入方法，与下方send方法对接

    

	public void initView()
    {
    	mListView = (ListView) findViewById(R.id.listview);
    	mBtnSend = (Button) findViewById(R.id.btn_send);
    	mBtnSend.setOnClickListener(this);
    	mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);
    	
    	mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }

    /*
    private String[]msgArray = new String[]{"有大吗", "有！你呢？", "我也有", "那上吧", 
    										"打啊！你放大啊", "你tm咋不放大呢？留大抢人头那！Cao的。你个菜b",
    										"2B不解释", "尼滚....",};
    
    private String[]dataArray = new String[]{"2012-09-01 18:00", "2012-09-01 18:10", 
    										"2012-09-01 18:11", "2012-09-01 18:20", 
    										"2012-09-01 18:30", "2012-09-01 18:35", 
    										"2012-09-01 18:40", "2012-09-01 18:50"}; 
    
    private static int COUNT=9;
    public void initData()
    {
    	for(int i = 0; i < COUNT; i++)
    	{
    		ChatMsgEntity entity = new ChatMsgEntity();
    		entity.setDate(dataArray[i]);
    		if (i % 2 == 0)
    		{
    			entity.setName("小黑");
    			entity.setMsgType(true);
    		}else{
    			entity.setName("人马");
    			entity.setMsgType(false);
    		}
    		
    		entity.setText(msgArray[i]);
    		mDataArrays.add(entity);
    	}

    	mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
		
    }
*/
	public void initData(){
		Cursor cur = mSQLDatabase.rawQuery("select * from table1", null);
		cur.moveToFirst();
		if(cur.moveToFirst()){
		for(int i = 0;i<2;i++,cur.moveToNext()){
			
			
				//更改——查询数据，目的是使initdata查询SQL里的数据
				int n=cur.getCount();
				
					
					id = cur.getInt((int)cur.getColumnIndex("id"));
					time = cur.getString((int)cur.getColumnIndex("time"));
					who = cur.getInt((int)cur.getColumnIndex("who"));
					word = cur.getString((int)cur.getColumnIndex("word"));
			
					 
				
			
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(time);
			if(who==0){
				entity.setName("黑暗游侠");
				entity.setMsgType(true);
			}
			else{
				entity.setName("半人马酋长");
				entity.setMsgType(false);
			}
			entity.setText(word);
			mDataArrays.add(entity);
		}
		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_send:
			send();
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}
	
	private void send()
	{
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0)
		{
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(getDate());
			entity.setName("人马");
			entity.setMsgType(false);
			entity.setText(contString);
			
			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();
			
			mEditTextContent.setText("");
			
			mListView.setSelection(mListView.getCount() - 1);
		}
	}
	
    private String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        
        
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins); 
        						
        						
        return sbBuffer.toString();
    }
    
    
    public void head_xiaohei(View v) {     //标题栏 返回按钮
    	Intent intent = new Intent (ChatActivity.this,InfoXiaohei.class);			
		startActivity(intent);	
      } 
}