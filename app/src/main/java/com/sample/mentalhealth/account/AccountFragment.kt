package com.sample.mentalhealth.account

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresPermission
import androidx.compose.ui.semantics.text
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.sample.mentalhealth.R
import com.sample.mentalhealth.common.Utility
import com.sample.mentalhealth.data.TaskDatabase
import com.sample.mentalhealth.databinding.FragAccBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.ntp.NTPUDPClient
import java.io.IOException
import java.net.InetAddress
import java.util.Date


class AccountFragment : Fragment() {

    private var _binding: FragAccBinding? = null
    private val binding get() = _binding!!
    private lateinit var accountViewModel: AccountViewModel

    val TIME_SERVER: String = "time-a.nist.gov"

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var deviceList: ArrayList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (BluetoothDevice.ACTION_FOUND == intent?.action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                Log.d("BT_SCAN", "Device detected = ${device?.address}")

                device?.let {
                    @SuppressLint("MissingPermission")
                    val name = if (hasBluetoothConnectPermission()) {
                        it.name ?: "Unknown"
                    } else {
                        "Permission Required"
                    }
                    val info = "$name - ${it.address}"

                    if (!deviceList.contains(info)) {
                        deviceList.add(info)
                        arrayAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragAccBinding.inflate(inflater, container, false)

        val accDao = TaskDatabase.getInstance(requireContext()).accountDao()
        val repository = AccountRepository(accDao)
        accountViewModel = AccountViewModelFactory(repository).create(AccountViewModel::class.java)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        deviceList = ArrayList()

        arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            deviceList
        )

// 3. Observe User Data and update UI
// Assuming your ViewModel has a 'userData' LiveData or similar method
        accountViewModel.getUserData().observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.userName.text = it.userName   // Matches your commented-out saveInfo logic
                binding.userMail.text = it.email
            }
        }

        /*binding.listView.adapter = arrayAdapter

        binding.btnScan.setOnClickListener {
            checkPermissionAndScan()
        }

        binding.btnSave.setOnClickListener {
            saveInfo()
            *//*CoroutineScope(Dispatchers.IO).launch {
                //get network time
                printTimes()
            }*//*
        }*/

        binding.btnSignOut.setOnClickListener {
            Utility().clearUserLoginData(requireContext())
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /*private fun saveInfo() {
        val userName = binding.userName.text.toString()
        val userMail = binding.userMail.text.toString()

        if(userName.isEmpty() || userMail.isEmpty()){
            Toast.makeText(context, "Enter all details", Toast.LENGTH_SHORT).show()
            return
        } else  if (!Patterns.EMAIL_ADDRESS.matcher(userMail).matches()) {
            Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            accountViewModel.insertData(userName, userMail)
            binding.userName.text?.clear()
            binding.userMail.text?.clear()
        }
    }*/

    @Throws(IOException::class)
    suspend fun printTimes() {
        try {
            val timeClient = NTPUDPClient()
            val inetAddress = InetAddress.getByName(TIME_SERVER)
            val timeInfo = timeClient.getTime(inetAddress)
            val returnTime = timeInfo.message.transmitTimeStamp.time

            val time: Date = Date(returnTime)
            withContext(Dispatchers.Main) {
                Log.e("getCurrentNetworkTime", "Time from $TIME_SERVER: $time")

                Log.e("Local time", "Local time")
                Log.e("Local time", "Current time: " + Date(System.currentTimeMillis()))
                Log.e("Local time", "Time info: " + Date(timeInfo.returnTime))
                Log.e(
                    "Local time",
                    "GetOriginateTimeStamp: " + Date(timeInfo.message.originateTimeStamp.time)
                )

                Log.e("NTP time", "Time from $TIME_SERVER: $time")

                Log.e("Local time", "Time info: " + Date(timeInfo.message.receiveTimeStamp.time))
                Log.e(
                    "Local time",
                    "GetOriginateTimeStamp: " + Date(timeInfo.message.transmitTimeStamp.time)
                )
            }
        } catch (e: Exception) {
            // Handle network errors
            withContext(Dispatchers.Main) {
                Log.e("Error", "Error: ${e.message}")
            }
        }
    }

    private fun hasBluetoothPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun hasBluetoothConnectPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun checkPermissionAndScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                ),
                101
            )
        } else {
            startScan()
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private fun startScan() {

        if (!hasAllPermissions()) {
            requestPermissions(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    )
                else
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            return
        }

        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }

        deviceList.clear()

        requireContext().registerReceiver(
            receiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )

        val started = bluetoothAdapter.startDiscovery()
        Log.d("BT", "Discovery started: $started")
    }


    private fun hasAllPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
        } else {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        try {
            requireContext().unregisterReceiver(receiver)
        } catch (_: Exception) { }
        _binding = null
    }

}