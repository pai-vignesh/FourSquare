package com.robosoft.foursquare.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.ActivityPlaceDetailsBinding
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.preferences.Preferences
import com.robosoft.foursquare.room.FavouriteModel
import com.robosoft.foursquare.util.CustomDialogClass
import com.robosoft.foursquare.util.LocationPermission
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.HomeViewModel
import com.robosoft.foursquare.viewmodel.PlaceDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import javax.inject.Inject


@AndroidEntryPoint
class PlaceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceDetailsBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: Location
    private val placeDetailsViewModel: PlaceDetailsViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels()

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var fsqId : String? = null
    lateinit var favouriteModel : FavouriteModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        fsqId = intent.getStringExtra("fsqId")
        binding.apply {
            topAppBar.setNavigationOnClickListener {
                onBackPressed()
            }
            addReview.setOnClickListener {
                val i = Intent(this@PlaceDetailsActivity, AddReview::class.java)
                startActivity(i)
            }
            reviews.setOnClickListener {
                val i = Intent(this@PlaceDetailsActivity, ReviewActivity::class.java)
                i.putExtra("fsqId", fsqId)
                i.putExtra("placeName",topAppBar.title)
                startActivity(i)
            }
            photos.setOnClickListener {
                val i = Intent(this@PlaceDetailsActivity, GalleryActivity::class.java)
                i.putExtra("fsqId", fsqId)
                i.putExtra("placeName", topAppBar.title)
                startActivity(i)
            }
            ratingTv?.setOnClickListener {
                val cdd = CustomDialogClass(this@PlaceDetailsActivity)
                val map = takeScreenShot(this@PlaceDetailsActivity)
                val fast = fastBlur(map!!, 120)
                val draw: Drawable = BitmapDrawable(resources, fast)
                cdd.window!!.setBackgroundDrawable(draw)
                cdd.show()
            }
        }
        fsqId?.let {
            placeDetailsViewModel.getPlaceDetails(it).observe(this) { data ->
                data?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            resource.data?.let { placeData ->
                                updateView(placeData)
                            }
                        }
                        Status.ERROR -> {
                        }
                    }
                }
            }
        }
    }

    private fun takeScreenShot(activity: Activity): Bitmap? {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val b1 = view.drawingCache
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight: Int = frame.top
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        Log.d("TAG", "takeScreenShot: $statusBarHeight ")
        display.getSize(size)
        val width: Int = size.x
        val height: Int = size.y
        val b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight)
        view.destroyDrawingCache()
        return b
    }

    private fun fastBlur(sentBitmap: Bitmap, radius: Int): Bitmap? {
        val bitmap = sentBitmap.copy(sentBitmap.config, true)
        if (radius < 1) {
            return null
        }
        val w = bitmap.width
        val h = bitmap.height
        val pix = IntArray(w * h)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)
        val wm = w - 1
        val hm = h - 1
        val wh = w * h
        val div = radius + radius + 1
        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)
        var rsum: Int
        var gsum: Int
        var bsum: Int
        var x: Int
        var y: Int
        var i: Int
        var p: Int
        var yp: Int
        var yi: Int
        var yw: Int
        val vmin = IntArray(Math.max(w, h))
        var divsum = div + 1 shr 1
        divsum *= divsum
        val dv = IntArray(256 * divsum)
        i = 0
        while (i < 256 * divsum) {
            dv[i] = i / divsum
            i++
        }
        yi = 0
        yw = yi
        val stack = Array(div) {
            IntArray(
                3
            )
        }
        var stackpointer: Int
        var stackstart: Int
        var sir: IntArray
        var rbs: Int
        val r1 = radius + 1
        var routsum: Int
        var goutsum: Int
        var boutsum: Int
        var rinsum: Int
        var ginsum: Int
        var binsum: Int
        y = 0
        while (y < h) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            i = -radius
            while (i <= radius) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))]
                sir = stack[i + radius]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rbs = r1 - Math.abs(i)
                rsum += sir[0] * rbs
                gsum += sir[1] * rbs
                bsum += sir[2] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                i++
            }
            stackpointer = radius
            x = 0
            while (x < w) {
                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm)
                }
                p = pix[yw + vmin[x]]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer % div]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi++
                x++
            }
            yw += w
            y++
        }
        x = 0
        while (x < w) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            yp = -radius * w
            i = -radius
            while (i <= radius) {
                yi = Math.max(0, yp) + x
                sir = stack[i + radius]
                sir[0] = r[yi]
                sir[1] = g[yi]
                sir[2] = b[yi]
                rbs = r1 - Math.abs(i)
                rsum += r[yi] * rbs
                gsum += g[yi] * rbs
                bsum += b[yi] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                if (i < hm) {
                    yp += w
                }
                i++
            }
            yi = x
            stackpointer = radius
            y = 0
            while (y < h) {

                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] =
                    -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w
                }
                p = x + vmin[y]
                sir[0] = r[p]
                sir[1] = g[p]
                sir[2] = b[p]
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi += w
                y++
            }
            x++
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h)
        return bitmap
    }


    private fun updateView(placeData: PlaceData){
        fetchLocation(
            placeData.geocodes.main.latitude.toDouble(),
            placeData.geocodes.main.longitude.toDouble()
        )
        var imageUrl = ""
        if (!placeData.photos.isNullOrEmpty()) {
            val requestOptions = RequestOptions().diskCacheStrategy(
                DiskCacheStrategy.ALL
            )
            val imgSize = "400x400"
            imageUrl =
                placeData.photos[0].prefix + imgSize + placeData.photos[0].suffix
            Glide.with(this).load(imageUrl).apply(requestOptions)
                .into(binding.placeImg)
        }
        binding.apply {
            topAppBar.title = placeData.name
            tvAddr.text = placeData.location.address
            tvLocality.text = placeData.location.locality
            placeData.rating?.let { stars ->
                ratingBar.rating = ((stars * 5) / 10).toFloat()
            }
            placeData.tel?.let { tel ->
                tvphone.text = tel
            }
            if (placeData.categories.isEmpty()) {
                placeType.text = "unknown type"
            } else {
                placeType.text = placeData.categories[0].name
            }
            var priceVal = "• ₹"
            placeData.price?.let { price ->
                priceVal = when (price) {
                    1 -> "• ₹"
                    2 -> "• ₹₹"
                    3 -> "• ₹₹₹"
                    4 -> "• ₹₹₹₹"
                    5 -> "• ₹₹₹₹₹"
                    else -> ""
                }
            }
            favouriteModel = FavouriteModel(
                placeData.fsqId,
                placeData.name,
                placeType.text.toString(),
                priceVal,
                placeData.geocodes.main.latitude,
                placeData.geocodes.main.longitude,
                ratingBar.rating.toString(),
                imageUrl,
                placeData.location.address
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.place_details_bar, menu)
        val fsqList = Preferences.getArrayPrefs("PlaceList", this)
        if (fsqList.contains(fsqId)) {
            menu.getItem(1).icon = ContextCompat.getDrawable(this, R.drawable.fav_selected)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share ->{
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
                i.putExtra(Intent.EXTRA_TEXT, "Thank you for using app")
                startActivity(Intent.createChooser(i, "Share URL"))
            }
            R.id.favorite ->{
                val fsqList = Preferences.getArrayPrefs("PlaceList", this)
                if (fsqList.contains(fsqId)) {
                    item.icon = ContextCompat.getDrawable(this, R.drawable.favourite)
                    fsqList.remove(fsqId)
                    Preferences.setArrayPrefs("PlaceList",fsqList,this)
                    homeViewModel.deleteFavourites(favouriteModel).observe(this) { dataInserted ->
                        dataInserted?.let { resource ->
                            when (resource.status) {
                                Status.LOADING -> {
                                }
                                Status.SUCCESS -> {
                                }
                                Status.ERROR -> {
                                }
                            }
                        }
                    }
                }else{
                    fsqList.add(fsqId)
                    Preferences.setArrayPrefs("PlaceList",fsqList,this)
                    item.icon = ContextCompat.getDrawable(this, R.drawable.fav_selected)
                    homeViewModel.insertFavourites(favouriteModel).observe(this) { dataInserted ->
                        dataInserted?.let { resource ->
                            when (resource.status) {
                                Status.LOADING -> {
                                }
                                Status.SUCCESS -> {
                                }
                                Status.ERROR -> {
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchLocation(lat: Double, lng: Double) {
        if (LocationPermission.checkPermission(this)) {
            val task = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener { location ->
                currentLocation = location
                mapFragment =
                    supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
                mapFragment.getMapAsync {
                    googleMap = it
                    val destLocation = Location("destination")
                    val myLocation = LatLng(
                        lat,
                        lng
                    )
                    destLocation.latitude = lat
                    destLocation.longitude = lng
                    val km = DecimalFormat("##.##").format(location.distanceTo(destLocation) / 1000)
                    binding.tvDistance.text = getString(R.string.distance_text, km)
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            myLocation,
                            15.5f
                        )
                    )
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(myLocation)
                            .title("Marker in Sydney")
                    )
                }
            }
        }
    }
}