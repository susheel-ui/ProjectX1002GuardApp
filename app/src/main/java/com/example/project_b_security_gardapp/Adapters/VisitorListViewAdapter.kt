package com.example.project_b_security_gardapp.Adapters

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.project_b_security_gardapp.R
import com.example.project_b_security_gardapp.Services.WebSocketHelper
import com.example.project_b_security_gardapp.VeiwRequestActivity
import com.example.project_b_security_gardapp.api.Entities.RequestsResultEntity
import com.example.project_b_security_gardapp.databinding.VisitorListLayoutBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.lang.Exception
import java.net.URI
import java.time.LocalDateTime
import java.util.Calendar
import java.util.concurrent.TimeUnit

class VisitorListViewAdapter(
    private val context: Context,
    private var visitorList: List<RequestsResultEntity>
) : RecyclerView.Adapter<VisitorListViewAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: VisitorListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("CheckResult")
        fun bind(visitor: RequestsResultEntity) {
            binding.tvGuestName.text = visitor.guestName
            binding.tvFlatNumber.text = visitor.flatNumber.toString()
//            binding.tvPhoneNumber.text = visitor.phoneNumber
//            binding.tvDescription.text = visitor.description
            binding.tvType.text = visitor.status
            val color = when (visitor.status) {
                "PENDING" -> ContextCompat.getColor(
                    context,
                    R.color.orange_yellow
                )   // orange/yellow
                "REJECTED" -> ContextCompat.getColor(context, R.color.red) // red
                else -> ContextCompat.getColor(context, R.color.green)       // green
            }
            binding.tvType.backgroundTintList = ColorStateList.valueOf(color)
            binding.root.setOnClickListener {
                val intent = Intent(context, VeiwRequestActivity::class.java)
                intent.putExtra("id", visitor.id.toString())
                context.startActivity(intent)
            }

//            WebSocketHelper.connect()
//            WebSocketHelper.subscribe("/topic/request/${visitor.id}") { message ->
//                Log.d(TAG, "bind: $message")
//            }


           }

//
//            try {
//                val timeAgoTxt = getTimeAgo(visitor.createdAt.toString())
//            Log.d(TAG, "bind: ${timeAgoTxt.toString()}")
//            binding.tvTimestamp.text = timeAgoTxt.toString()
//            }catch (e:Exception){
//                binding.tvTimestamp.text = "N/A"
//                Log.d(TAG, "bind: ${e.message}")
//            }
            // ✅ Load guest image (if available)
//            Glide.with(context)
//                .load("http://your-server-ip:8080" + visitor.photo1)
//                .placeholder(com.example.project_b_security_gardapp.R.drawable.placeholder)
//                .into(binding.ivGuestImage)

            // ✅ Optional click listener


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            VisitorListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = visitorList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(visitorList[position])
    }

    // ✅ Call this to update the list from ViewModel
    fun updateData(newList: List<RequestsResultEntity>) {
        visitorList = newList
        notifyDataSetChanged()
    }

    fun getTimeAgo(createdAt: String): String {
        val parts = createdAt.removeSurrounding("[", "]").split(",").map { it.trim().toInt() }
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, parts[0])
            set(Calendar.MONTH, parts[1] - 1)
            set(Calendar.DAY_OF_MONTH, parts[2])
            set(Calendar.HOUR_OF_DAY, parts[3])
            set(Calendar.MINUTE, parts[4])
            set(Calendar.SECOND, parts[5])
            set(Calendar.MILLISECOND, parts[6] / 1_000_000)
        }

        val diffMillis = System.currentTimeMillis() - cal.timeInMillis
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffMillis)

        return when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "$minutes min ago"
            hours < 24 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            else -> "$days day${if (days > 1) "s" else ""} ago"
        }
    }
}
