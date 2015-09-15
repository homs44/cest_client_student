package kr.devy.cest.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * Created by pc on 2015-08-02.
 */
public class BluetoothService {

    public static final int STATE_IDLE = 1;
    public static final int STATE_SCANNING = 2;

    public static final int START_SCAN  = 1;
    public static final int FOUND_MAC_ADDRESS  = 2;
    public static final int STOP_SCAN  = 3;

    private int state;
    private BluetoothAdapter adapter;
    private static final long SCAN_PERIOD = 10000;
    private Handler handler;
    MScanCallback callback;
    String mac_addresses;

    private static BluetoothService instance;

    public static BluetoothService getInstance(Context context,Handler handler, String mac_addresses){
        if(instance ==null){
            instance = new BluetoothService(context);
        }
        instance.init(handler,mac_addresses);
        return instance;
    }
    private BluetoothService(Context context){
        adapter = ((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        callback = new MScanCallback();

    }

    private void init(Handler handler, String mac_addresses) {
        this.handler = handler;
        this.mac_addresses = mac_addresses;
        callback.init(mac_addresses,handler);
    }

    public boolean isEnabled() {
        if (adapter == null || !adapter.isEnabled()) {
            return false;
        } else {
            return true;
        }
    }

    public void scanDevice(final boolean enable) {

        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    state = STATE_IDLE;
                    adapter.stopLeScan(callback);
                }
            }, SCAN_PERIOD);
            state = STATE_SCANNING;
            adapter.startLeScan(callback);
            handler.sendEmptyMessage(START_SCAN);
        } else {
            state = STATE_IDLE;
            adapter.stopLeScan(callback);
            handler.sendEmptyMessage(STOP_SCAN);
        }
    }


    public class MScanCallback implements BluetoothAdapter.LeScanCallback {


        String mac_addresses;
        Handler handler;

        public void init(String mac_addresses, Handler handler) {
            this.mac_addresses = mac_addresses;
            this.handler = handler;
        }

        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            Log.d("cest", "found mac : " + bluetoothDevice.getAddress());

            if(mac_addresses!=null) {
                if (mac_addresses.contains(bluetoothDevice.getAddress())) {
                    handler.sendEmptyMessage(FOUND_MAC_ADDRESS);
                    scanDevice(false);
                }
            }
        }


    }


}
