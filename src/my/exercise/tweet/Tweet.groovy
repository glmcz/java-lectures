package my.exercise.tweet

import groovy.transform.ToString

@ToString
class Tweet {
    Integer id
    String title
    String body
    List hashTags = [] // need to be initialized otherwise we can add to them nothing
    List comments = []
    Integer likes
    Integer shares
}
