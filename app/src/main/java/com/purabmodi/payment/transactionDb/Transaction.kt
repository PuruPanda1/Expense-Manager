package com.purabmodi.payment.transactionDb

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val tDescription:String,
    val incomeAmount: Float,
    val isExpense:Int,
    val date: Long,
    val transactionType:String,
    val day:Int,
    val week:Int,
    val month:Int,
    val year:Int,
    val expenseAmount: Float,
    val modeOfPayment: String
):Parcelable
