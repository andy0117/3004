var DEBUG_MODE = true;

$(function() {
	/**
	* Toggle visibility of comparitive options
	*/
	$("input.comparitivebox").live("change", function() {
		$(this).parent().parent().find(".comparitive").toggle();
	});
	
	/**
	* Toggle visibility of ranking options
	*/
	$("input.rankingbox").live("change", function() {
		$(this).parent().parent().find(".ranking").toggle();
	});
	
	var question = $("div.question").clone();
	
	/**
	* Add a new question
	*/
	$("#newq").click(function() {
		question.clone().appendTo(document.body);
	});
	
	/**
	* Add a new response
	*/
	$("button.newr").live("click", function() {
		var parent = $(this).parent(),
			response = $(this).next();
		response.clone().appendTo(parent);
	});
	
	/**
	* Cancel a question
	*/
	$("button.remq").live("click", function() {
		$(this).parent().remove();
	});
	
	/**
	* Cancel a response
	*/
	$("button.remr").live("click", function() {
		$(this).parent().remove();
	});
});