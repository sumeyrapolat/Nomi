package com.sumeyrapolat.nomi.util

import android.content.ContentProviderOperation
import android.content.Context
import android.provider.ContactsContract
import com.sumeyrapolat.nomi.domain.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun saveContactToPhone(context: Context, contact: Contact) {
    withContext(Dispatchers.IO) {
        val ops = ArrayList<ContentProviderOperation>()

        // 👤 Yeni kişi ekle
        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        // 📛 Ad (Display Name)
        val displayName = "${contact.firstName} ${contact.lastName}".trim()
        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName)
                .build()
        )

        // 📞 Telefon numarası
        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.phoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build()
        )

        // 📷 Profil resmi (isteğe bağlı)
        // (profileImageUrl bir URI string ise ileride Base64 ekleme yapılabilir)

        try {
            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
