// Given an array of integers nums and an integer target, 
// return indices of the two numbers such that they add up to target.
// Input: nums = [2, 7, 11, 15], target = 9
// Output: [0, 1]


// for(0 , n) for( i + 1 , n) -> arr[i] + arr[j] == target -> (i , j);
// O(n * n ) -> O(n ^ 2)

int n = nums.length ;
// ArrayList<Integer> lst = new ArrayList<>() ;
// for(int i = 0 ; i < n ; i ++){
//     for(int j = i + 1 ; j < n ; j ++){
//         if(arr[i] + arr[j] == target){
//             lst.add(i);
//             lst.add(j);
//             return lst ;
//         }
//     }
// }

// return lst ;


// nums = [2, 7, 11, 15], target = 9 
// O(nlong) + O(n) => O(nlogn); 
// O(1);

// val -> idx 
//  2 -> 0
//  {0 , 1};




// int start = 0 , end = n - 1;
// while(start < end){
//     if(arr[start] + arr[end] == target){
//         lst.add(i);
//         lst.add(j);
//         return lst ;
//     }
//     else if(arr[start] + arr[end] > target){
//         end -- ;
//     }
//     else{
//         start ++ ;
//     }
// }




// String s ="102BSDTY74RSK"

// s[i] >= 0 && s[i] <= 9
// s[i] - '0' ;

// 1 -> 126
// 0 -> 125 ;

// 9 -> 134 

// int sum = 0 ;
// for(Char c : s){
//     if(c >= '1' && c <= '9'){
//         sum += (c - '0');
//     } 
// }
// sysout(sum);

Name        AGE     Salary      department
Alice       42      44000       IT
Bob         34      45000       Operations
Charlie     21      150000      Finance
Dale        56      23000       Engineering


// select * from employee order by Salary DESC offset 3 limit 1 ;

import javax.annotation.processing.Generated;

// @Generated (Generated.strategy = ) 