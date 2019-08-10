package com.example.macrocounterremaster.utils

import android.app.Activity
import com.example.macrocounterremaster.R

class ErrorMapCreator {
    companion object{
        fun getHashMap(activity: Activity): HashMap<String, String>{
            val hashMap: HashMap<String, String> = HashMap()
            hashMap["0"] = activity.getString(R.string.internet_unavailable)
            hashMap["200"] = activity.getString(R.string.ok)
            hashMap["201"] = activity.getString(R.string.created)
            hashMap["202"] = activity.getString(R.string.accepted)
            hashMap["204"] = activity.getString(R.string.no_content)
            hashMap["205"] = activity.getString(R.string.reset_content)
            hashMap["206"] = activity.getString(R.string.partial_content)
            hashMap["207"] = activity.getString(R.string.multi_status)
            hashMap["208"] = activity.getString(R.string.already_reported)
            hashMap["226"] = activity.getString(R.string.im_used)
            hashMap["300"] = activity.getString(R.string.multiple_choices)
            hashMap["301"] = activity.getString(R.string.moved_permanently)
            hashMap["302"] = activity.getString(R.string.found)
            hashMap["303"] = activity.getString(R.string.see_other)
            hashMap["304"] = activity.getString(R.string.not_modified)
            hashMap["305"] = activity.getString(R.string.use_proxy)
            hashMap["306"] = activity.getString(R.string.unused)
            hashMap["307"] = activity.getString(R.string.temporary_redirect)
            hashMap["308"] = activity.getString(R.string.permanent_redirect)
            hashMap["400"] = activity.getString(R.string.bad_request)
            hashMap["401"] = activity.getString(R.string.unauthorized)
            hashMap["402"] = activity.getString(R.string.payment_required)
            hashMap["403"] = activity.getString(R.string.forbidden)
            hashMap["404"] = activity.getString(R.string.not_found)
            hashMap["405"] = activity.getString(R.string.method_not_allowed)
            hashMap["406"] = activity.getString(R.string.not_acceptable)
            hashMap["407"] = activity.getString(R.string.proxy_authentication_required)
            hashMap["408"] = activity.getString(R.string.request_timeout)
            hashMap["409"] = activity.getString(R.string.conflict)
            hashMap["410"] = activity.getString(R.string.gone)
            hashMap["411"] = activity.getString(R.string.length_required)
            hashMap["412"] = activity.getString(R.string.precondition_failed)
            hashMap["413"] = activity.getString(R.string.request_entity_too_large)
            hashMap["414"] = activity.getString(R.string.request_uri_too_large)
            hashMap["415"] = activity.getString(R.string.unsupported_media_type)
            hashMap["416"] = activity.getString(R.string.request_range_unsatisfied)
            hashMap["417"] = activity.getString(R.string.expectation_failed)
            hashMap["426"] = activity.getString(R.string.upgrade_required)
            hashMap["428"] = activity.getString(R.string.precondition_required)
            hashMap["429"] = activity.getString(R.string.too_many_requests)
            hashMap["431"] = activity.getString(R.string.request_headers_too_large)
            hashMap["500"] = activity.getString(R.string.internal_server_error)
            hashMap["501"] = activity.getString(R.string.not_implemented)
            hashMap["502"] = activity.getString(R.string.bad_gateway)
            hashMap["503"] = activity.getString(R.string.service_unavailable)
            hashMap["504"] = activity.getString(R.string.gateway_timeout)
            hashMap["511"] = activity.getString(R.string.network_authentication_required)
            hashMap["598"] = activity.getString(R.string.network_read_timeout_error)
            hashMap["599"] = activity.getString(R.string.network_connect_timeout_error)

            return hashMap
        }
    }
}