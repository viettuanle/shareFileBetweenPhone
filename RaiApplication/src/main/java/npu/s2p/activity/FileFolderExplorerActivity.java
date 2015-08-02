package npu.s2p.activity;

import npu.support.adapter.FolderInfo;
import npu.support.adapter.ListViewFolderAdapter;
import npu.support.utility.FileTools;
import npu.support.utility.FileUtility;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.app.Activity;

import java.io.File;

import java.util.ArrayList;

import android.os.Bundle;
//import android.os.Handler;

import android.view.ContextMenu;
//import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.ListView;

import android.widget.TextView;
import android.content.Intent;

/**
 * @author Le Tuan
 * 
 *         Action: Copy and Move: Whenever user choose copy, or move, it will
 *         display button to choose location, otherwise this button is auto
 *         disappear. Next version: use quick action
 **/
public class FileFolderExplorerActivity extends Activity implements
		OnItemClickListener {
	private ArrayList<FolderInfo> list = null;

	private ListViewFolderAdapter adapter;// is a extend of ArrayAdapter
	private ListView ltvDisplay;
	// private Handler handler = new Handler();
	private String root = "/";

	private TextView myPath;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.explorer);

		myPath = (TextView) findViewById(R.id.path);

		ltvDisplay = (ListView) findViewById(R.id.lvtFileFolder);
		ltvDisplay.setOnItemClickListener(this);
	
		getDir(root);

		// create contextmenu for listview
		registerForContextMenu(ltvDisplay);

	}// end onCreate

	private void getDir(String dirPath) {

		myPath.setText("Location: " + dirPath);

		list = new ArrayList<FolderInfo>();
		File f = new File(dirPath);

		File[] files = f.listFiles();

		if (!dirPath.equals(root))

		{
			FolderInfo folder = new FolderInfo(root, root, "folder");
			list.add(folder);

			FolderInfo folder1 = new FolderInfo("../", f.getParent(), "folder");
			list.add(folder1);

		}

		for (int i = 0; i < files.length; i++) {
			String file_folder_name, is_file;
			String filesize_lastmodify, filepath;
			File file = files[i];

			if (file.isDirectory()) { // a directory then content name, path
				file_folder_name = file.getName() + "/";
				is_file = "folder";
				filepath = file.getPath();
				FolderInfo folder = new FolderInfo(file_folder_name, filepath,
						is_file);
				list.add(folder);
			} else {// a file then content name, path, filesize+lastdateModified
				file_folder_name = file.getName();
				filepath = file.getPath();
				FileUtility fu = new FileUtility(filepath);
				filesize_lastmodify = fu.getFileSize() + ", "
						+ fu.getLastModified();// hien thi size va ngay cap nhat
				is_file = "file";

				FolderInfo folder = new FolderInfo(file_folder_name, filepath,
						is_file, filesize_lastmodify);
				list.add(folder);
			}

		}

		displayList();

	}// end getDir

	private void displayList() {
		this.adapter = new ListViewFolderAdapter(this,
				android.R.layout.simple_list_item_1, list);
		refreshListView();// refresh listview
	}// end displayList

	public void refreshListView() {// before call this method, should update
									// content of list
		adapter.notifyDataSetChanged();// refresh listview
		ltvDisplay.setAdapter(adapter);
		ltvDisplay.invalidateViews();
	}

	@Override
	public void onItemClick(AdapterView arg0, View v, int position, long arg3) {

		final File file = new File(list.get(position).getPath());

		if (file.isDirectory()) {
			if (file.canRead()) {
				getDir(list.get(position).getPath());
			} else {
				new AlertDialog.Builder(this)
						.setIcon(R.drawable.foldercantsend)
						.setTitle("[" + file.getName() + "] can't be send!")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								}).show();
			}
		} else // is a file,
		{
			String filename_path = list.get(position).getPath();// ex: --->
																// /sdcard/myfile.txt
			FileUtility fileutility = new FileUtility(filename_path);
			Intent intent = getIntent();
			intent.putExtra("file_path", filename_path);// return path_file
			intent.putExtra("file_name", file.getName());// myfile.txt
			intent.putExtra("file_size", fileutility.getFileSize());//
			intent.putExtra("file_type", fileutility.getExt());
			setResult(RESULT_OK, intent);

			finish();

		}

	}// end onListItemClick

	// //////////////////////////////handle context menu of listview
	// ///////////////
	// create context menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.lvtFileFolder) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

			menu.setHeaderTitle((String) list.get(info.position)
					.getFoldername());// put name of current select into Title
			menu.add(0, v.getId(), 0, "Move");
			menu.add(0, v.getId(), 0, "Copy");
			menu.add(0, v.getId(), 0, "Rename");
			menu.add(0, v.getId(), 0, "Delete");
			menu.add(0, v.getId(), 0, "Cancel");

		}

	}

	// //////handle action with each select on context
	// menu///////////////////////
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		FolderInfo folderinfo = new FolderInfo(list.get(info.position));

		String filefolder_path = folderinfo.getPath();
		// File testfile = new File(filefolder_path);
		if (new File(filefolder_path).isDirectory()) {
			Toast.makeText(getBaseContext(), "There is no action for folder!",
					Toast.LENGTH_SHORT).show();

		} else {
			if (item.getTitle() == "Delete") {
				DeleteItem(filefolder_path);
			} else if (item.getTitle() == "Copy") {
				CopyItem(filefolder_path);
			} else if (item.getTitle() == "Rename") {
				RenameItem(filefolder_path);
			} else if (item.getTitle() == "Move") {
				MoveItem(filefolder_path);
			} else {
				return false;
			}
		}
		return true;
	} // end onContextItemSelected

	public void MoveItem(final String org_filefolder_path) {
		// display button when choose item Move. Check path, if file exist then
		// copy file with (+1) at extension, otherwise, just copy
		final Button bntCopy_Move_Explorer;
		bntCopy_Move_Explorer = (Button) findViewById(R.id.btnCopy_Move_Explorer);
		bntCopy_Move_Explorer.setText("Move to here?");
		bntCopy_Move_Explorer.setVisibility(0);

		bntCopy_Move_Explorer.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				String parent_path;// parent always is a directory
				int position_colon = myPath.getText().toString()
						.lastIndexOf(":");
				parent_path = myPath
						.getText()
						.toString()
						.substring(position_colon + 2,
								myPath.getText().toString().length());
				FileUtility fileold = new FileUtility(org_filefolder_path);
				if (!parent_path.equals(root)) {// remove 1 back slack / in case
												// copy file into root
					parent_path = parent_path + "/";
				}
				String temp_path;
				temp_path = parent_path + fileold.getFileName();
				File des_file = new File(temp_path);

				int i = 0;
				while (des_file.exists()) {
					i++;
					temp_path = parent_path + fileold.getFilenameWithoutExt()
							+ "(" + i + ")." + fileold.getExt();
					des_file = new File(temp_path);
				}
				Toast.makeText(getBaseContext(),
						"Copy" + new String(temp_path), Toast.LENGTH_LONG)
						.show();
				FileTools.moveFile(new File(org_filefolder_path), des_file);
				// invisible when action done.
				bntCopy_Move_Explorer.setVisibility(View.GONE);
				getDir(des_file.getParent());// for refresh
			}// end onClick

		});
	}// end MoveItem

	public void CopyItem(final String org_filefolder_path) {
		// display button when choose item copy. Check path, if file exist then
		// copy file with (+1) at extension, otherwise, just copy
		final Button bntCopy_Move_Explorer;
		bntCopy_Move_Explorer = (Button) findViewById(R.id.btnCopy_Move_Explorer);
		bntCopy_Move_Explorer.setText("Paste here?");
		bntCopy_Move_Explorer.setVisibility(0);

		bntCopy_Move_Explorer.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				String parent_path;// parent always is a directory
				int position_colon = myPath.getText().toString()
						.lastIndexOf(":");
				parent_path = myPath
						.getText()
						.toString()
						.substring(position_colon + 2,
								myPath.getText().toString().length());
				FileUtility fileold = new FileUtility(org_filefolder_path);
				if (!parent_path.equals(root)) {// remove 1 back slack / in case
												// copy file into root
					parent_path = parent_path + "/";
				}
				String temp_path;
				temp_path = parent_path + fileold.getFileName();
				File des_file = new File(temp_path);

				int i = 0;
				while (des_file.exists()) {
					i++;
					temp_path = parent_path + fileold.getFilenameWithoutExt()
							+ "(" + i + ")." + fileold.getExt();

					des_file = new File(temp_path);

				}
				Toast.makeText(getBaseContext(),
						"Copy" + new String(temp_path), Toast.LENGTH_LONG)
						.show();
				FileTools.copyFile(new File(org_filefolder_path), des_file);
				// invisible when action done.
				bntCopy_Move_Explorer.setVisibility(View.GONE);
				getDir(des_file.getParent());// for refresh
			}// end onClick

		});

	}

	public void RenameItem(String filefolder_path) {

		show_custom_dialog(filefolder_path);

	}

	public void DeleteItem(String filefolder_path) {
		// Precondition: file have to exist
		File file = new File(filefolder_path);
		String parent_path = file.getParent();
		file.delete();
		getDir(parent_path);// for refresh

	}

	public void show_custom_dialog(String file_folder_path) {
		final FileUtility file = new FileUtility(file_folder_path);
		final String file_name = file.getFileName();
		int dotposition = file_name.lastIndexOf(".");

		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.rename_custom_dialog);
		dialog.setTitle("Rename file!");
		dialog.setCancelable(true);

		TextView text = (TextView) dialog
				.findViewById(R.id.txtRename_CustomDialog_FileFolderExplorer);
		text.setText("Old name: " + file_name);// get oldname here

		final EditText edt_NewName = (EditText) dialog
				.findViewById(R.id.edtRename_CustomDialog_FileFolderExplorer);
		// bold the old_name here (not bold ext), request focus
		edt_NewName.requestFocus();
		edt_NewName.setText(file_name);
		edt_NewName.setSelection(0, dotposition);

		Button btnOK_Rename = (Button) dialog
				.findViewById(R.id.btnOK_Rename_CustomDialog_FileFolderExplorer);
		btnOK_Rename.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// check samename, check exist name, otherwise rename
				if (!edt_NewName.getText().toString().equals(file_name)) {
					String new_file_path = "/sdcard/"
							+ edt_NewName.getText().toString();
					File newfile = new File(new_file_path);
					if (!newfile.exists()) {
						file.rename(new_file_path);
						Toast.makeText(getBaseContext(), "change new name",
								Toast.LENGTH_LONG).show();

						getDir(newfile.getParent());// for refresh
					} else {
						Toast.makeText(getBaseContext(),
								"file has exist, please try again! ",
								Toast.LENGTH_LONG).show();
					}
				}
				dialog.dismiss();
			}
		});

		Button btnCancel_Rename = (Button) dialog
				.findViewById(R.id.btnCancel_Rename_CustomDialog_FileFolderExplorer);
		btnCancel_Rename.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	// //////////////////////////////handle click back key////////////////
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		openQuitDialog();
	}

	private void openQuitDialog() {
		AlertDialog.Builder quitDialog = new AlertDialog.Builder(
				FileFolderExplorerActivity.this);
		quitDialog.setTitle("Confirm to Quit?");

		quitDialog.setPositiveButton("OK, Quit!", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				FileFolderExplorerActivity.this.finish();
				FileFolderExplorerActivity.this.moveTaskToBack(true);
				// finish();
			}
		});

		quitDialog.setNegativeButton("NO", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		quitDialog.show();
	}

}// end class FileFolderExplorerActivity
