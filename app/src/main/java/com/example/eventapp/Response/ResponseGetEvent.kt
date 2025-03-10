package com.example.eventapp.Response

import com.example.eventapp.favoriteeventdata.FavoriteEvent
import com.google.gson.annotations.SerializedName

data class ResponseGetEvent(

	@field:SerializedName("listEvents")
	val listEvents: List<ListEventsItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

fun ListEventsItem.toFavoriteEvent(): FavoriteEvent {
	return FavoriteEvent(
		id = this.id,
		name = this.name,
		beginTime = this.beginTime,
		mediaCover = this.mediaCover,
		link = this.link
	)
}

data class ListEventsItem(

	@field:SerializedName("summary")
	val summary: String,

	@field:SerializedName("mediaCover")
	val mediaCover: String,

	@field:SerializedName("registrants")
	val registrants: Int,

	@field:SerializedName("imageLogo")
	val imageLogo: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("ownerName")
	val ownerName: String,

	@field:SerializedName("cityName")
	val cityName: String,

	@field:SerializedName("quota")
	val quota: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("beginTime")
	val beginTime: String,

	@field:SerializedName("endTime")
	val endTime: String,

	@field:SerializedName("category")
	val category: String
)
