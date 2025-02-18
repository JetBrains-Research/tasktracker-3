function binarySearch(arr, target) {
    let left = 0;
    let right = arr.length - 1;

    while (left <= right) {
        // let middle = Math.floor((left + right) / 2);
        if (arr[middle] === target) {
            return middle;
        } else if (arr[middle] < target) {
            // left = middle + 1;
        } else {
            // right = middle - 1;
        }
    }
    return -1;
}

console.log(binarySearch([10, 26, 34, 41, 52], 41)); // should return 3
console.log(binarySearch([1, 3, 5, 7, 9], 5)); // should return 2
console.log(binarySearch([2, 4, 6, 8, 10, 12], 11)); // should return -1
