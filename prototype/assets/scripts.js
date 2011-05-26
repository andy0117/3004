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
	
	var question = $("div.question").clone(),
		questionCount = 1;
	
	/**
	* Add a new question
	*/
	$("#newq").click(function() {
		question.clone().appendTo(document.body);
		questionCount++;
	});
	
	/**
	* Add a new response
	*/
	$("button.newr").live("click", function() {
		var parent = $(this).next(),
			response = $(this).next().children(":first-child");
		response.clone().appendTo(parent);
	});
	
	/**
	* Cancel a question. Not allowed to remove all questions.
	*/
	$("button.remq").live("click", function() {
		if(questionCount > 1) {
			$(this).parent().remove();
			questionCount--;
		}
	});
	
	/**
	* Cancel a response
	*/
	$("button.remr").live("click", function() {
		if($(this).parent().parent().children("div").size() > 1) {
			$(this).parent().remove();
		}
	});
});