/**
 * Created by icondor on 16/07/16.
 */

/**
 * Created by condor on 03/04/15.
 */

function listFeed(feeds) { // feeds means all rows in the db
    var myFeedHTML = document.getElementById('myFeedHTML')
    var listHtml = ''; // a temporary to store the content

    for (var i = 0; i < feeds.length; i++) {
        var feed = feeds[i]; // one row in db
        var oneHTMLRow =
            '<h2>' +
            feed.content+'   '+
            feed.insertDate+
            '</h2>';
        listHtml += oneHTMLRow;
    }
    myFeedHTML.innerHTML = listHtml;
}

function loadFeed() {
    $.ajax({
        url: 'myfeed'
    }).done(function (response) {
        listFeed(response.myFeed);
    });
}


function addTweet() {
    var toDoText = document.getElementById('tweet').value;
    $.ajax({
        url: 'addtweet?textToTweet='+toDoText
    }).done(function (response) {
        location.href = "index.html";
    });
}
function follow() {
    var toFriend = document.getElementById('friend').value;
    $.ajax({
        url: 'follow?numeFriend='+toFriend
    }).done(function (response) {
        location.href = "index.html";
    });
}

