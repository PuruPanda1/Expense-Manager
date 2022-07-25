package com.example.payment.transactionDb

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize

@VersionedParcelize
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val tDescription:String,
    val incomeAmount: Float,
    val isExpense:Boolean,
    val date: Long,
    val transactionType:String,
    val day:Int,
    val week:Int,
    val month:Int,
    val expenseAmount: Float
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readByte() != 0.toByte(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat()
    )
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}
