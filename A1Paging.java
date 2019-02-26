//Huba Ferenc Benzar

import java.util.*;
import java.io.*;

class A1Paging {

	private static Scanner keyboardInput = new Scanner (System.in);
	private static final int maxCacheSize = 10;
	private static final int maxRequest = 100;


	// Do NOT change the main method!
	// main program
	public static void main(String[] args) throws Exception {
		int count=0, size=0;
		int[] org_cache = new int[maxCacheSize];
		int[] cache = new int[maxCacheSize];
		int[] request = new int[maxRequest];
		
		init_array(org_cache, maxCacheSize, -1);
		init_array(request, maxRequest, 0);

		// get the cache size and the number of requests 
		// then get the corresponding input in the respective arrays
		try {
			System.out.println();
			System.out.print("Enter the cache size (1-" + maxCacheSize + "): ");
			size = keyboardInput.nextInt();
			System.out.print("Enter the content of the cache (" + size + " different +ve integers): ");
			for (int i=0; i<size; i++)
				org_cache[i] = keyboardInput.nextInt();				
			System.out.println();
			System.out.print("Enter the number of page requests: (1-" + maxRequest + "): ");
			count = keyboardInput.nextInt();
			if (count > maxRequest || count <= 0)
				System.exit(0);
			System.out.print("Enter " + count + " +ve integers: ");
			for (int i=0; i<count; i++)
				request[i] = keyboardInput.nextInt();				
		}
		catch (Exception e) {
			keyboardInput.next();
			System.exit(0);
		}
/*		
		System.out.println();
		System.out.println("Cache content: ");
		print_array(org_cache, size);
		System.out.println("Request sequence: ");
		print_array(request, count);
*/
		
		copy_array(org_cache, cache, size);
		no_evict(cache, size, request, count);
		copy_array(org_cache, cache, size);
		evict_largest(cache, size, request, count);
		copy_array(org_cache, cache, size);
		evict_fifo(cache, size, request, count);
		copy_array(org_cache, cache, size);
		evict_lfu(cache, size, request, count);

	}
	
	// Do NOT change this method!
	// set array[0]..array[n-1] to 0
	static void init_array(int[] array, int n, int value) {
		for (int i=0; i<n; i++) 
			array[i] = value;
	}
	
	// Do NOT change this method!
	// set array[0]..array[n-1] to 0
	static void print_array(int[] array, int n) {
		for (int i=0; i<n; i++) {
			System.out.print(array[i] + " ");
			if (i%10 == 9)
				System.out.println();
		}
		System.out.println();
	}
	
	// Do NOT change this method!
	// copy n numbers from array a1 to array a2, starting from a1[x1] and a2[x2]
	static void copy_array(int[] a1, int[] a2, int n) {
		for (int i=0; i<n; i++) {
			a2[i] = a1[i];
		}
	}	

	
	
	
	
//Task 1
	static void no_evict(int[] cache, int c_size, int[] request, int r_size) {
		//Declerations
		int missCounter = 0, hitCounter = 0, cacheTrack = 0;
		String hitmissChar = "";
		
		for (int requestTrack = 0; requestTrack<r_size;){
			if (request[requestTrack] == cache [cacheTrack]){
				//Counts to hit
				hitmissChar = hitmissChar + "h";
				hitCounter++;
				//Array Trackers
				requestTrack++;
				cacheTrack = 0;
			}
			else if (cacheTrack > c_size){
				//Counts to miss
				hitmissChar = hitmissChar + "m";
				missCounter++;
				//Array Trackers
				cacheTrack = 0;
				//itterates requestTrack
				requestTrack++;
			}
			else {
				//cacheTrack + 1
				cacheTrack++;
			}
		}
		//Print out results for hit and miss and count them.
		System.out.println("\n" + hitmissChar + "\n" + hitCounter + " h " + missCounter + " m\n");
	}

	
	
	
	
//Task 2
	static void evict_largest(int[] cache, int c_size, int[] request, int r_size) {
		//Declerations
		int hitCounter = 0, index = 0, cacheTrack = 0, maxTrack = 0, missCounter = 0;
		String hitmissChar = "";
		
		for (int requestTrack = 0; requestTrack<r_size;){
			if (request[requestTrack] == cache [cacheTrack]){
				//Counts to hit
				hitmissChar = hitmissChar + "h";
				hitCounter++;
				//Array Trackers
				requestTrack++;
				cacheTrack = 0;
			}
			else if (cacheTrack > c_size){
				for (index = 0; index < c_size;) {
					if (cache[index] > cache[(index + 1)]) {
					//Add one to cache index in the array keeps track of maximum
						maxTrack = cache[1];
						cache[index] = cache[(index + 1)];
						cache[(index + 1)] = maxTrack;
						//index + 1
						index++;
						}
						else if (cache[1] <= cache[(index + 1)]){
							//index + 1
							index++;
						}
				}
				//Counts to miss
				hitmissChar = hitmissChar + "m";
				missCounter++;
				//Cache Tracker = 0
				cacheTrack = 0;
				//Change index to request track
				cache[index] = request[requestTrack];
				//Request Tracker + 1
				requestTrack++;
			}
			else {
				//cacheTrack + 1
				cacheTrack++;
			}
		}
		//Print out results for hit and miss and count them.
		System.out.println(hitmissChar + "\n" + hitCounter + " h " + missCounter + " m\n");
	}

	
	
	
	
//Task 3
	static void evict_fifo(int[] cache, int c_size, int[] request, int r_size) {
		//Declerations
		int hitCounter = 0, index = 0, cacheTrack = 0, maxTrack = 0, missCounter = 0, first = 0;
		String hitmissChar = "";
	
		for (int requestTrack = 0; requestTrack<r_size;){
			if (request[requestTrack] == cache[cacheTrack]){
				//Counts to hits
				hitmissChar = hitmissChar + "h";
				hitCounter++;
				//Array Trackers
				requestTrack++;
				cacheTrack = 0;
			}
			else if (cacheTrack >= c_size-1){
				//Counts to misses
				hitmissChar = hitmissChar + "m";
				missCounter++;
				if (first > c_size-1){
					//first set to 0
					first = 0;
					//Add one to cache index in the array and finds out what the maximum index is.
					maxTrack = cache[1];
					cache[requestTrack] = cache[(requestTrack + 1)];
					cache[(requestTrack + 1)] = maxTrack;
					cacheTrack = 0;
					//Iterating
					requestTrack++;
					first++;
				}
				else{
					//Cache Tracker = 0
					cacheTrack = 0;
					//Cache first element is 0
					cache[first] = 0;
					//Change index to request track
					maxTrack = request[requestTrack];
					//cache first is the highest value
					cache[first] = maxTrack;
					//incrementation
					requestTrack++;
					first++;
				}
			}
			else {
				//cacheTrack + 1
				cacheTrack++;
			}
		}
		//Print out results for hit and miss and count them.
		System.out.println(hitmissChar + "\n" + hitCounter + " h " + missCounter + " m\n");
	}
	
	
	
	
	
//Task 4
	static void evict_lfu(int[] cache, int c_size, int[] request, int r_size) {
		//Declarations
		int hitCounter = 0, missCounter = 0,firstIndex = 1, cacheTrack = 0;
		int[] lfuTrack = new int[c_size];
		String hitmissChar = "";
		
		for (int requestTrack = 0; requestTrack < r_size;) {
			//set index to -1
			int index = -1;
			for (cacheTrack = 0; cacheTrack < c_size;) {
				if (cache[cacheTrack] == request[requestTrack]) {
					index = cacheTrack;
					//breaks out the for loop
					break;
				}
				//cacheTrack + 1
				cacheTrack++;
			}
			//when index is not -1
			if (index != -1) { 
				//Counters to hit
				hitmissChar = hitmissChar + "h";
				hitCounter++;
				//Itterates the array
				lfuTrack[index]++;
				
			} 
			else {
				//Counters to hit
				hitmissChar = hitmissChar + "m"; 
				missCounter++;
				
				//declaring smallest
				int smallest = Integer.MAX_VALUE;
				for (cacheTrack = 0; cacheTrack < c_size;) {
					if (lfuTrack[cacheTrack] < smallest) {
						//This tracks the smallest index
						smallest = lfuTrack[cacheTrack];
						firstIndex = cacheTrack;
					}
					//CacheTrack + 1
					cacheTrack++;
				}
				//The cache becomes the requested array
				cache[firstIndex] = request[requestTrack];
				//LFU is reset to 0
				lfuTrack[firstIndex] = 0;
			}
			//requestTrack+1
			requestTrack++;
		}
		//Print out results for hit and miss and count them.
		System.out.println(hitmissChar + "\n" + hitCounter + " h " + missCounter + " m");
	}
}
