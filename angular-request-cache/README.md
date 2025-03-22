# Request caching

## Progressive Web App

This feature adds a great many capabilities, potentially including request caching, but does not appear to include the feature we are looking for WRT re-requesting data periodically. This is probably a capability worth adding to our UIs but it will not do what we are looking for by itself. While it may allow caching of requests it will still cause repeated calls to the function that contains the requests and that fills up the memory real quick. 

## Pure RxJS solution

Although solutions exist here most seem to be marked as deprecated. I abandoned this solution early on.

## RxResource

This is a new feature in Angular 19 and considered experimental. This would be a problem if we had large and extensive UIs but I think we can handle making an update to Angular 20 with these present even if there are some fairly substantive changes to how they work.
