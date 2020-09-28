/**
* Author: Krishna Modi
* Contact: krish512@hotmail.com
*/
package com.krish512.k_exchange;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import 	androidx.appcompat.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.krish512.k_exchange.Utils.AppState;
import com.krish512.k_exchange.Utils.LoadData;
import com.krish512.k_exchange.Utils.Operation;
import com.krish512.k_exchange.R;

public class AddPropertyActivity extends AppCompatActivity {

	private enum dd {
		CITY, TOWN, LOCALITY, TYPE, FLOOR
	};

	dd ddSection;
	int currentCity = -1;
	int currentTown = -1;
	int currentLocality = -1;
	int selected = -1;

	ProgressBar pbProgress;
	TextView ddCity;
	TextView ddTown;
	TextView ddLocality;
	TextView ddType;
	TextView ddFloor;
	TextView txtCost;
	TextView txtRent;
	TextView txtDeposit;
	EditText editAddress;
	EditText editArea;
	EditText editCost;
	EditText editRent;
	EditText editDeposit;
	TextView valExtraInfo;
	RadioGroup radioSellRent;
	RadioButton radioSell;
	RadioButton radioRent;
	RadioGroup radioResCom;
	RadioButton radioRes;
	RadioButton radioCom;
	RadioGroup radioDirectSide;
	RadioButton radioDirect;
	RadioButton radioSide;
	RadioButton radioTmp;
	String OptionalInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addproperty);

		Button btnAddProperty = (Button) findViewById(R.id.btnAddProperty);
		TextView btnCancel = (TextView) findViewById(R.id.txtCancel);
		TextView btnInfoShow = (TextView) findViewById(R.id.txtInfoShow);
		radioSellRent = (RadioGroup) findViewById(R.id.radioSellRent);
		radioSell = (RadioButton) findViewById(R.id.radioSell);
		radioRent = (RadioButton) findViewById(R.id.radioRent);
		radioResCom = (RadioGroup) findViewById(R.id.radioResCom);
		radioRes = (RadioButton) findViewById(R.id.radioRes);
		radioCom = (RadioButton) findViewById(R.id.radioCom);
		radioDirectSide = (RadioGroup) findViewById(R.id.radioDirectSide);
		radioDirect = (RadioButton) findViewById(R.id.radioDirect);
		radioSide = (RadioButton) findViewById(R.id.radioSide);
		// RadioButton radioSell = (RadioButton) findViewById(R.id.radioSell);
		// RadioButton radioRent = (RadioButton) findViewById(R.id.radioRent);
		txtCost = (TextView) findViewById(R.id.txtCost);
		txtRent = (TextView) findViewById(R.id.txtRent);
		txtDeposit = (TextView) findViewById(R.id.txtDeposit);
		valExtraInfo = (TextView) findViewById(R.id.valExtraInfo);
		editCost = (EditText) findViewById(R.id.editCost);
		editRent = (EditText) findViewById(R.id.editRent);
		editDeposit = (EditText) findViewById(R.id.editDeposit);
		editAddress = (EditText) findViewById(R.id.editAddress);
		editArea = (EditText) findViewById(R.id.editArea);

		ddCity = (TextView) findViewById(R.id.ddCity);
		ddTown = (TextView) findViewById(R.id.ddTown);
		ddLocality = (TextView) findViewById(R.id.ddLocality);
		ddType = (TextView) findViewById(R.id.ddType);
		ddFloor = (TextView) findViewById(R.id.ddFloor);
		pbProgress = (ProgressBar) findViewById(R.id.pbProgess);

		txtCost.setVisibility(View.VISIBLE);
		editCost.setVisibility(View.VISIBLE);
		txtRent.setVisibility(View.GONE);
		editRent.setVisibility(View.GONE);
		txtDeposit.setVisibility(View.GONE);
		editDeposit.setVisibility(View.GONE);
		pbProgress.setVisibility(View.GONE);

		btnAddProperty.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				// Create the intent to start another activity
				selected = radioSellRent.getCheckedRadioButtonId();
				radioTmp = (RadioButton) findViewById(selected);
				OptionalInfo = AppState.temp;
				pbProgress.setVisibility(View.VISIBLE);

				if (validate()) {
					new MyAsyncTask().execute();
				} else {
					pbProgress.setVisibility(View.GONE);
				}
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				// Create the intent to start another activity
				Intent intent = new Intent(getBaseContext(),
						MyPropertyActivity.class);
				startActivity(intent);
				finish();
			}
		});

		btnInfoShow.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				// Create the intent to start another activity
				AppState.temp = OptionalInfo;
				Intent intent = new Intent(getBaseContext(),
						OptionalInfoActivity.class);
				startActivityForResult(intent, 1);
				// startActivity(intent);
			}
		});

		radioSellRent.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				// String x = ;
				if (checkedId == R.id.radioSell) {
					txtCost.setVisibility(View.VISIBLE);
					editCost.setVisibility(View.VISIBLE);
					txtRent.setVisibility(View.GONE);
					editRent.setVisibility(View.GONE);
					txtDeposit.setVisibility(View.GONE);
					editDeposit.setVisibility(View.GONE);
				} else if (checkedId == R.id.radioRent) {
					txtCost.setVisibility(View.GONE);
					editCost.setVisibility(View.GONE);
					txtRent.setVisibility(View.VISIBLE);
					editRent.setVisibility(View.VISIBLE);
					txtDeposit.setVisibility(View.VISIBLE);
					editDeposit.setVisibility(View.VISIBLE);
				}
			}
		});

		ddCity.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				// Create the intent to start another activity
				ddSection = dd.CITY;
				registerForContextMenu(findViewById(android.R.id.content));
				openContextMenu(findViewById(android.R.id.content));
			}
		});

		ddTown.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				// Create the intent to start another activity
				ddSection = dd.TOWN;
				registerForContextMenu(findViewById(android.R.id.content));
				openContextMenu(findViewById(android.R.id.content));
			}
		});

		ddLocality.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				// Create the intent to start another activity
				ddSection = dd.LOCALITY;
				registerForContextMenu(findViewById(android.R.id.content));
				openContextMenu(findViewById(android.R.id.content));
			}
		});

		ddType.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				// Create the intent to start another activity
				ddSection = dd.TYPE;
				registerForContextMenu(findViewById(android.R.id.content));
				openContextMenu(findViewById(android.R.id.content));
			}
		});

		ddFloor.setOnClickListener(new OnClickListener() {
			@Override
			// On click function
			public void onClick(View view) {
				// Create the intent to start another activity
				ddSection = dd.FLOOR;
				registerForContextMenu(findViewById(android.R.id.content));
				openContextMenu(findViewById(android.R.id.content));
			}
		});
	}

	private boolean validate() {

		if ((radioSell.isChecked() == false)
				&& (radioRent.isChecked() == false)) {
			radioSell.setTextColor(getResources().getColor(R.color.themeRED));
			radioRent.setTextColor(getResources().getColor(R.color.themeRED));
			Toast toast = Toast.makeText(getBaseContext(),
					"Error: Select Sell or Rent", Toast.LENGTH_LONG);
			toast.show();
			return false;
		} else {
			radioSell.setTextColor(getResources().getColor(R.color.themeBLACK));
			radioRent.setTextColor(getResources().getColor(R.color.themeBLACK));
		}

		if ((radioRes.isChecked() == false) && (radioCom.isChecked() == false)) {
			radioRes.setTextColor(getResources().getColor(R.color.themeRED));
			radioCom.setTextColor(getResources().getColor(R.color.themeRED));
			Toast toast = Toast.makeText(getBaseContext(),
					"Error: Select Residential or Commercial",
					Toast.LENGTH_LONG);
			toast.show();
			return false;
		} else {
			radioRes.setTextColor(getResources().getColor(R.color.themeBLACK));
			radioCom.setTextColor(getResources().getColor(R.color.themeBLACK));
		}

		if ((radioDirect.isChecked() == false)
				&& (radioSide.isChecked() == false)) {
			radioDirect.setTextColor(getResources().getColor(R.color.themeRED));
			radioSide.setTextColor(getResources().getColor(R.color.themeRED));
			Toast toast = Toast.makeText(getBaseContext(),
					"Error: Select Sharing or Side by Side", Toast.LENGTH_LONG);
			toast.show();
			return false;
		} else {
			radioDirect.setTextColor(getResources()
					.getColor(R.color.themeBLACK));
			radioSide.setTextColor(getResources().getColor(R.color.themeBLACK));
		}

		if (ddCity.getText().toString().contentEquals("")) {
			ddCity.setHint("Required");
			ddCity.setHintTextColor(getResources().getColor(R.color.themeRED));
			Toast toast = Toast.makeText(getBaseContext(),
					"Error: Select City", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (ddTown.getText().toString().contentEquals("")) {
			ddTown.setHint("Required");
			ddTown.setHintTextColor(getResources().getColor(R.color.themeRED));
			Toast toast = Toast.makeText(getBaseContext(),
					"Error: Select Town", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (ddLocality.getText().toString().contentEquals("")) {
			ddLocality.setHint("Required");
			ddLocality.setHintTextColor(getResources().getColor(
					R.color.themeRED));
			Toast toast = Toast.makeText(getBaseContext(),
					"Error: Select Locality", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (ddType.getText().toString().contentEquals("")) {
			ddType.setHint("Required");
			ddType.setHintTextColor(getResources().getColor(R.color.themeRED));
			Toast toast = Toast.makeText(getBaseContext(),
					"Error: Select Type", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (ddFloor.getText().toString().contentEquals("")) {
			ddFloor.setHint("Required");
			ddFloor.setHintTextColor(getResources().getColor(R.color.themeRED));
			Toast toast = Toast.makeText(getBaseContext(),
					"Error: Select Floor", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (editArea.getText().toString().contentEquals("")) {
			editArea.setHint("Required");
			editArea.setHintTextColor(getResources().getColor(R.color.themeRED));
			Toast toast = Toast.makeText(getBaseContext(),
					"Error: Fill up Area", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (editAddress.getText().toString().contentEquals("")) {
			editAddress.setHint("Required");
			editAddress.setHintTextColor(getResources().getColor(
					R.color.themeRED));
			Toast toast = Toast.makeText(getBaseContext(),
					"Error: Fill up Address", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		if (editCost.getText().toString().contentEquals("")
				&& editRent.getText().toString().contentEquals("")) {
			Toast toast = Toast.makeText(getBaseContext(),
					"Error: Fill up cost details", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		return true;
	}

	private class MyAsyncTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... param) {
			String response = null;
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("uid", AppState.UID));
			params.add(new BasicNameValuePair("sellrent", radioTmp.getText()
					.toString()));
			selected = radioResCom.getCheckedRadioButtonId();
			radioTmp = (RadioButton) findViewById(selected);
			params.add(new BasicNameValuePair("rescom", radioTmp.getText()
					.toString()));
			selected = radioDirectSide.getCheckedRadioButtonId();
			radioTmp = (RadioButton) findViewById(selected);
			params.add(new BasicNameValuePair("directside", radioTmp.getText()
					.toString()));
			params.add(new BasicNameValuePair("optionalinfo", OptionalInfo));
			params.add(new BasicNameValuePair("city",
					LoadData.CityTown.cities[currentCity].city));
			params.add(new BasicNameValuePair(
					"town",
					LoadData.CityTown.cities[currentCity].towns[currentTown].town));
			params.add(new BasicNameValuePair("locality", ddLocality.getText()
					.toString()));
			params.add(new BasicNameValuePair("address", editAddress.getText()
					.toString()));
			params.add(new BasicNameValuePair("area", editArea.getText()
					.toString()));
			params.add(new BasicNameValuePair("type", ddType.getText()
					.toString()));
			params.add(new BasicNameValuePair("floor", ddFloor.getText()
					.toString()));
			if (radioSell.isChecked()) {
				params.add(new BasicNameValuePair("cost", editCost.getText()
						.toString()));
				params.add(new BasicNameValuePair("rent", "0"));
				params.add(new BasicNameValuePair("deposit", "0"));
			} else {
				params.add(new BasicNameValuePair("cost", "0"));
				params.add(new BasicNameValuePair("rent", editRent.getText()
						.toString()));
				params.add(new BasicNameValuePair("deposit", editDeposit
						.getText().toString()));
			}
			try {
				response = Operation.postHttpResponse(new URI(
						AppState.absoluteUri + "addproperties.php"), params);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String[] reply = response.split(":");
			if (reply[0].equalsIgnoreCase("Success") == true) {
				return null;
			} else {
				return response.toString();
			}
		}

		protected void onPostExecute(String result) {
			if (result != null) {
				String[] reply = result.split(":");
				Toast toast = Toast.makeText(getBaseContext(), "Failed: "
						+ reply[1], Toast.LENGTH_LONG);
				toast.show();
			} else {
				LoadData.MyProperties.properties = null;
				LoadData.Properties.properties = null;
				AppState.PropertiesCount += 1;
				Intent intent = new Intent(getBaseContext(),
						MyPropertyActivity.class);
				startActivity(intent);
				finish();
				AppState.temp = null;
			}
			pbProgress.setVisibility(View.GONE);
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getBaseContext(), MyPropertyActivity.class);
		startActivity(intent);
		finish();
		AppState.temp = null;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.removeItem(android.R.id.switchInputMethod);
		if (ddSection == dd.CITY) {
			menu.setHeaderTitle("City");
			// getMenuInflater().inflate(R.menu.city, menu);
			for (int i = 0; i < LoadData.CityTown.cities.length; i++) {
				menu.add(0, i, 0, LoadData.CityTown.cities[i].city);
			}
		} else if (ddSection == dd.TOWN) {
			menu.setHeaderTitle("Town");
			// getMenuInflater().inflate(R.menu.town, menu);
			if (currentCity != -1) {
				for (int i = 0; i < LoadData.CityTown.cities[currentCity].towns.length; i++) {
					menu.add(0, i, 0,
							LoadData.CityTown.cities[currentCity].towns[i].town);
				}
			}
		} else if (ddSection == dd.LOCALITY) {
			menu.setHeaderTitle("Locality");
			// getMenuInflater().inflate(R.menu.locality, menu);
			if ((currentCity != -1) && (currentTown != -1)) {
				for (int i = 0; i < LoadData.CityTown.cities[currentCity].towns[currentTown].localities.length; i++) {
					menu.add(
							0,
							i,
							0,
							LoadData.CityTown.cities[currentCity].towns[currentTown].localities[i].locality);
				}
			}
		} else if (ddSection == dd.TYPE) {
			menu.setHeaderTitle("Type");
			if (radioResCom.getCheckedRadioButtonId() == radioRes.getId()) {
				getMenuInflater().inflate(R.menu.typeres, menu);
			} else {
				getMenuInflater().inflate(R.menu.typecom, menu);
			}
		} else if (ddSection == dd.FLOOR) {
			menu.setHeaderTitle("Floor");
			getMenuInflater().inflate(R.menu.floor, menu);
		} else {
			menu.setHeaderTitle("Sort");
			getMenuInflater().inflate(R.menu.sort, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (ddSection == dd.CITY) {
			currentCity = item.getItemId();
			ddCity.setText(LoadData.CityTown.cities[currentCity].city);
			ddTown.setText("");
			currentTown = -1;
			return true;
		} else if (ddSection == dd.TOWN) {
			currentTown = item.getItemId();
			ddTown.setText(LoadData.CityTown.cities[currentCity].towns[currentTown].town);
			ddLocality.setText("");
			currentLocality = -1;
			return true;
		} else if (ddSection == dd.LOCALITY) {
			currentLocality = item.getItemId();
			ddLocality
					.setText(LoadData.CityTown.cities[currentCity].towns[currentTown].localities[currentLocality].locality);
			return true;
		} else if (ddSection == dd.TYPE) {
			ddType.setText(item.getTitle());
			return true;
		} else if (ddSection == dd.FLOOR) {
			ddFloor.setText(item.getTitle());
			return true;
		} else {
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_property, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if (requestCode == 1) {
		// if(resultCode == RESULT_OK){
		// String result=data.getStringExtra("result");
		// }
		// if (resultCode == RESULT_CANCELED) {
		// //Write your code if there's no result
		// }
		// }
		OptionalInfo = AppState.temp;
		valExtraInfo.setText(OptionalInfo);
	}

}
