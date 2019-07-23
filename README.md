# Musical Artist Recommendation

This is a system that recommends musical artists for an user based on their listening habits. The project was the main assignment of the DIM0124 - PROGRAMAÇÃO CONCORRENTE (concurrent programming) course. Several versions were developed:

- [sequential](sequential/)
	It's an implementation that doesn't use any concurrency techniques.
- [vanilla](vanilla/)
	It contains the versions developed using "pure" Java concurrency techniques such as `synchronized`, semaphores, ForkJoin etc., one for each technique.
- [akka](akka/)
	This version uses the Akka and Cassandra frameworks.
- [play](play/)
	This version is an extension of the [akka](akka/) one which uses the Play framework.
