package com.novoda.merlin;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.novoda.merlin.service.AndroidVersion;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(JUnit4.class)
public class MerlinsBeardShould {

    private static final boolean DISCONNECTED = false;
    private static final boolean CONNECTED = true;

    @Mock
    private ConnectivityManager mockConnectivityManager;

    @Mock
    private NetworkInfo mockNetworkInfo;

    @Mock
    private AndroidVersion mockAndroidVersion;

    private MerlinsBeard merlinsBeard;

    @Before
    public void setUp() {
        initMocks(this);

        merlinsBeard = new MerlinsBeard(mockConnectivityManager, mockAndroidVersion);
    }

    @Test
    public void returnFalseForIsConnectedWhenNetworkConnectionIsUnavailable() {
        when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(false);

        boolean connected = merlinsBeard.isConnected();

        assertThat(connected).isFalse();
    }

    @Test
    public void returnFalseForIsConnectedWhenNetworkConnectionIsNull() {
        when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(null);

        boolean connected = merlinsBeard.isConnected();

        assertThat(connected).isFalse();
    }

    @Test
    public void returnTrueForIsConnectedWhenNetworkConnectionIsAvailable() {
        when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(true);

        boolean connected = merlinsBeard.isConnected();

        assertThat(connected).isTrue();
    }

    @Test
    public void returnTrueWhenConnectedToWifiAndAndroidVersionIsBelowMarshmallow() {
        when(mockAndroidVersion.isMarshmallowOrHigher()).thenReturn(false);
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(true);

        boolean connectedToWifi = merlinsBeard.isConnectedToWifi();

        assertThat(connectedToWifi).isTrue();
    }

    @Test
    public void returnFalseWhenNotConnectedToWifiAndAndroidVersionIsBelowMarshmallow() {
        when(mockAndroidVersion.isMarshmallowOrHigher()).thenReturn(false);
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(false);

        boolean connectedToWifi = merlinsBeard.isConnectedToWifi();

        assertThat(connectedToWifi).isFalse();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Test
    public void returnTrueWhenConnectedToWifiAndAndroidVersionIsMarshmallowOrAbove() {
        givenNetworkState(CONNECTED, ConnectivityManager.TYPE_WIFI);

        boolean connectedToWifi = merlinsBeard.isConnectedToWifi();

        assertThat(connectedToWifi).isTrue();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Test
    public void returnWhenConnectedToWifiAndAndroidVersionIsMarshmallowOrAbove() {
        givenNetworkState(DISCONNECTED, ConnectivityManager.TYPE_WIFI);

        boolean connectedToWifi = merlinsBeard.isConnectedToWifi();

        assertThat(connectedToWifi).isFalse();
    }

    @Test
    public void returnFalseWhenConnectionIsNotAvailable() {
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(null);

        boolean connectedToWifi = merlinsBeard.isConnectedToWifi();

        assertThat(connectedToWifi).isFalse();
    }

    @Test
    public void returnTrueWhenConnectedToMobileNetworkAndAndroidVersionIsBelowMarshmallow() {
        when(mockAndroidVersion.isMarshmallowOrHigher()).thenReturn(false);
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(true);

        boolean connectedToconnectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork();

        assertThat(connectedToconnectedToMobileNetwork).isTrue();
    }

    @Test
    public void returnFalseWhenNotConnectedToMobileNetworkAndAndroidVersionIsBelowMarshmallow() {
        when(mockAndroidVersion.isMarshmallowOrHigher()).thenReturn(false);
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(false);

        boolean connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork();

        assertThat(connectedToMobileNetwork).isFalse();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Test
    public void returnTrueWhenConnectedToMobileNetworkAndAndroidVersionIsMarshmallowOrAbove() {
        givenNetworkState(CONNECTED, ConnectivityManager.TYPE_MOBILE);

        boolean connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork();

        assertThat(connectedToMobileNetwork).isTrue();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Test
    public void returnFalseWhenNotConnectedToMobileNetworkAndAndroidVersionIsMarshmallowOrAbove() {
        givenNetworkState(DISCONNECTED, ConnectivityManager.TYPE_MOBILE);

        boolean connectedToMobileNetwork = merlinsBeard.isConnectedToMobileNetwork();

        assertThat(connectedToMobileNetwork).isFalse();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void givenNetworkState(boolean isConnected, int networkType) {
        Network network = Mockito.mock(Network.class);
        NetworkInfo networkInfo = Mockito.mock(NetworkInfo.class);
        when(networkInfo.getType()).thenReturn(networkType);
        when(networkInfo.isConnected()).thenReturn(isConnected);
        when(mockConnectivityManager.getNetworkInfo(network)).thenReturn(networkInfo);
        when(mockAndroidVersion.isMarshmallowOrHigher()).thenReturn(true);
        when(mockConnectivityManager.getAllNetworks()).thenReturn(new Network[]{network});
    }

}
