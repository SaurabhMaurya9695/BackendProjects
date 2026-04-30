# KMP (Knuth-Morris-Pratt) Algorithm

## 📌 Quick Summary
KMP is an **optimal pattern matching algorithm** that finds all occurrences of a pattern in a text with **O(n + m)** time complexity, avoiding the naive O(n*m) approach.

---

## 🎯 When To Use KMP

| Scenario | Use KMP? |
|----------|----------|
| Find pattern in text (single match) | ✅ Yes |
| Find all occurrences of pattern | ✅ Yes |
| Check if pattern exists | ✅ Yes |
| String period/repetition detection | ✅ Yes |
| Suffix-prefix overlap problems | ✅ Yes |
| Real-time pattern matching | ✅ Yes |
| Simple substring existence | ❌ No (use `.indexOf()` for simplicity) |

---

## 🧠 How KMP Works

### Naive Approach (❌ O(n*m))
```
Text:     a b a b c a b c a b a b a b d
Pattern:  a b a b d
                    ↑ Mismatch, restart from idx 1
```
When mismatch occurs, restart from the next position. This causes re-checking.

### KMP Approach (✅ O(n+m))
Instead of restarting, use **LPS (Longest Proper Prefix which is also Suffix)** array to jump intelligently.

```
Text:     a b a b c a b c a b a b a b d
Pattern:  a b a b d
                    ↑ Mismatch, use LPS to jump
LPS:      [0, 0, 1, 2, 0]  ← tells us how much overlap exists
```

---

## 📚 Core Concept: LPS Array

### What is LPS?
For each position `i` in the pattern, LPS[i] = length of the **longest proper prefix that is also a suffix** (excluding the character itself).

#### Example Walkthrough
```
Pattern: "ababd"

Position 0 ('a'): LPS[0] = 0
  → No proper prefix or suffix exists

Position 1 ('ab'): LPS[1] = 0
  → Prefix: "a", Suffix: "b" → No match

Position 2 ('aba'): LPS[2] = 1
  → Prefix: "a", Suffix: "a" → Match! LPS[2] = 1

Position 3 ('abab'): LPS[3] = 2
  → Prefix: "ab", Suffix: "ab" → Match! LPS[3] = 2

Position 4 ('ababd'): LPS[4] = 0
  → Prefix: "a","ab","aba","abab"
  → Suffix: "d","bd","abd","babd" → No match

Final LPS: [0, 0, 1, 2, 0]
```

---

## 🔧 Algorithm Breakdown

### Step 1: Build LPS Array (O(m))
```java
public static int[] buildLPS(String s) {
    int n = s.length();
    int[] lps = new int[n];
    int len = 0;  // length of prefix that matched
    int i = 1;    // start from position 1

    while (i < n) {
        if (s.charAt(i) == s.charAt(len)) {
            // Characters match, extend the prefix
            len++;
            lps[i] = len;
            i++;
        } else {
            // Mismatch, fall back using previous LPS value
            if (len != 0) {
                len = lps[len - 1];  // KEY: Don't restart, use LPS fallback
            } else {
                lps[i] = 0;
                i++;
            }
        }
    }
    return lps;
}
```

### Step 2: Match Pattern in Text (O(n))
```java
int iIdx = 0;  // pointer for text
int jIdx = 0;  // pointer for pattern

while (iIdx < s.length()) {
    // Characters match, advance both pointers
    if (s.charAt(iIdx) == p.charAt(jIdx)) {
        iIdx++;
        jIdx++;
    }

    // Pattern found
    if (jIdx == p.length()) {
        // Record match at position (iIdx - jIdx)
        jIdx = lps[jIdx - 1];  // Continue searching for overlapping matches
    }
    // Mismatch occurred
    else if (iIdx < s.length() && s.charAt(iIdx) != p.charAt(jIdx)) {
        if (jIdx != 0) {
            jIdx = lps[jIdx - 1];  // Jump using LPS, don't increment iIdx
        } else {
            iIdx++;  // Only increment text pointer
        }
    }
}
```

---

## ⚡ Complexity Analysis

| Aspect | Complexity | Why |
|--------|-----------|-----|
| **Time** | **O(n + m)** | LPS array takes O(m), matching takes O(n), each char visited once |
| **Space** | **O(m)** | LPS array storage |
| **Naive (brute force)** | O(n * m) | Restart from beginning on every mismatch |

### Real Numbers Example
- Text: 10,000 chars
- Pattern: 100 chars
- **Naive**: ~1,000,000 comparisons (worst case)
- **KMP**: ~10,100 comparisons ✅

---

## 🎯 Common Competitive Programming Problems

### 1. **Pattern Matching** (Basic)
Find if pattern exists in text.
```
Input: s = "ababcabcabababd", p = "ababd"
Output: "MATCH FOUND AT IDX 10"
```

### 2. **Find All Occurrences** (Important!)
```java
// Modify matching section:
if (jIdx == p.length()) {
    System.out.println("Match at: " + (iIdx - jIdx));
    jIdx = lps[jIdx - 1];  // Continue for overlapping matches
}
```

### 3. **Count Non-Overlapping Matches**
```java
if (jIdx == p.length()) {
    count++;
    jIdx = 0;  // Don't use LPS, skip pattern entirely
}
```

### 4. **Find Period of String**
Use KMP to find if string is periodic.
```
Pattern: "ab"
Text: "abababab"
If pattern matches at positions 0,2,4,6 with equal spacing → periodic
Period = 2
```

### 5. **Shortest Palindrome** (Hard - requires KMP variant)
Concatenate: `s + "#" + reverse(s)`, compute LPS
```
s = "abcd"
Compute LPS for "abcd#dcba"
```

### 6. **String Rotation Detection**
```
s1 = "waterbottle"
s2 = "erbottlewat"
Check if s2 exists in s1 + s1 using KMP
```

---

## ⚠️ Common Mistakes (Competitive Programming)

### ❌ Mistake 1: Not Handling Overlapping Matches
```java
// WRONG: Resets to 0, skips overlapping matches
if (jIdx == p.length()) {
    System.out.println("Match at: " + (iIdx - jIdx));
    jIdx = 0;  // ❌ Misses overlapping occurrences
}

// RIGHT: Uses LPS to continue
if (jIdx == p.length()) {
    System.out.println("Match at: " + (iIdx - jIdx));
    jIdx = lps[jIdx - 1];  // ✅ Continues searching
}
```

### ❌ Mistake 2: Incrementing iIdx After Mismatch
```java
// WRONG: Double increment
if (s.charAt(iIdx) != p.charAt(jIdx)) {
    jIdx = lps[jIdx - 1];
    iIdx++;  // ❌ Causes skipping
}

// RIGHT: Only increment if fallback to 0
if (jIdx != 0) {
    jIdx = lps[jIdx - 1];
} else {
    iIdx++;  // ✅ Only increment here
}
```

### ❌ Mistake 3: Wrong LPS Edge Case
```java
// WRONG: Doesn't handle len = 0 properly
lps[i] = len;  // ❌ Will set to non-zero when it should be 0

// RIGHT: Explicit check
if (len != 0) {
    len = lps[len - 1];
} else {
    lps[i] = 0;  // ✅ Explicitly set to 0
}
```

### ❌ Mistake 4: Modifying Pattern After Build
```java
// WRONG: Pattern changes mid-execution
int[] lps = buildLPS(p);
p = p + "x";  // ❌ LPS array now invalid
```

---

## 🧪 Test Cases You Should Try

```java
// Case 1: Pattern at beginning
s = "pattern_text", p = "pattern" → Index 0 ✅

// Case 2: Pattern at end
s = "text_pattern", p = "pattern" → Index 5 ✅

// Case 3: Overlapping matches
s = "aaaa", p = "aa" → Index 0, 1, 2 ✅

// Case 4: Pattern not found
s = "xyz", p = "abc" → Not found ✅

// Case 5: Pattern equals text
s = "abc", p = "abc" → Index 0 ✅

// Case 6: Single character
s = "aaaa", p = "a" → Index 0, 1, 2, 3 ✅

// Case 7: Pattern longer than text
s = "ab", p = "abcd" → Not found ✅

// Case 8: Empty pattern (edge)
s = "abc", p = "" → Should handle gracefully ⚠️

// Case 9: Repeating patterns
s = "ababababab", p = "abab" → Multiple matches ✅

// Case 10: Pattern with internal repetition
s = "aaaaaa", p = "aaa" → Overlapping matches ✅
```

---

## 🔥 LeetCode Problem: Repeated Substring Pattern (MUST KNOW!)

**Problem Link**: https://leetcode.com/problems/repeated-substring-pattern/

### Problem Statement
Given a string `s`, determine if it can be constructed from a repeating substring.

**Examples**:
```
Input: s = "abab"      → Output: true  (constructed from "ab" × 2)
Input: s = "aba"       → Output: false (no repeating pattern)
Input: s = "abcabcabcabc"  → Output: true (constructed from "abc" × 4)
Input: s = "a"         → Output: false (single char can't repeat)
```

### ⚡ Why KMP is PERFECT for This Problem

Most people try **brute force** (try all divisors) or **string trick** `(s+s).substring(1, s.length()+1).contains(s)`. But **KMP's LPS array gives the ELEGANT solution**!

### 🧠 The Mathematical Insight

**KEY IDEA**: If a string has a repeating pattern, then:
1. **LPS[n-1]** (last value of LPS array) will be **non-zero**
2. The repeating pattern length = **n - LPS[n-1]**
3. This pattern length **must divide n evenly**

#### Why Does This Work?

If `LPS[n-1] = k`, it means:
- The first `k` characters of the string match the **last `k` characters**
- This overlap reveals the repeating unit!

```
String: "abcabcabc"
Length: 9

LPS Array: [0, 0, 0, 1, 2, 3, 4, 5, 6]
                                      ↑ LPS[8] = 6

Pattern Length = 9 - 6 = 3 ✅
Check: 9 % 3 = 0 ✅
Pattern: "abc" (repeated 3 times)
```

### 📊 Detailed Walkthrough: "abab"

```
String: "abab"
Index:   0123
n = 4

Building LPS Array:

i=1: 'b' vs 'a' → Mismatch → LPS[1] = 0
i=2: 'a' vs 'a' → Match → len=1, LPS[2] = 1
i=3: 'b' vs 'b' → Match → len=2, LPS[3] = 2

Final LPS: [0, 0, 1, 2]
           ↑        ↑
         Start     Last (LPS[n-1] = 2)

Analysis:
• LPS[3] = 2 (non-zero, so potentially repeating)
• Pattern Length = 4 - 2 = 2
• Check: 4 % 2 = 0 ✅ (divides evenly)
• Pattern: "ab" (repeated 2 times)

Result: TRUE ✅
```

### 📊 Another Walkthrough: "aba"

```
String: "aba"
Index:   012
n = 3

Building LPS Array:

i=1: 'b' vs 'a' → Mismatch → LPS[1] = 0
i=2: 'a' vs 'a' → Match → len=1, LPS[2] = 1

Final LPS: [0, 0, 1]
           ↑        ↑
         Start     Last (LPS[n-1] = 1)

Analysis:
• LPS[2] = 1 (non-zero, but let's check divisibility)
• Pattern Length = 3 - 1 = 2
• Check: 3 % 2 = 1 ✗ (doesn't divide evenly!)
• So it's NOT a valid repeating pattern

Result: FALSE ✅
```

### 💻 Java Implementation Using KMP

```java
public boolean repeatedSubstringPattern(String s) {
    int n = s.length();
    int[] lps = buildLPS(s);
    
    // If LPS[n-1] is 0, no repeating pattern exists
    if (lps[n - 1] == 0) {
        return false;
    }
    
    // Pattern length = total length - overlapping length
    int patternLen = n - lps[n - 1];
    
    // Pattern length must divide total length evenly
    return n % patternLen == 0;
}

// Standard KMP LPS building
private int[] buildLPS(String s) {
    int n = s.length();
    int[] lps = new int[n];
    int len = 0;
    int i = 1;
    
    while (i < n) {
        if (s.charAt(i) == s.charAt(len)) {
            len++;
            lps[i] = len;
            i++;
        } else {
            if (len != 0) {
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }
    }
    return lps;
}
```

### 📈 Complexity Analysis

| Approach | Time | Space | Why |
|----------|------|-------|-----|
| **KMP** | **O(n)** | **O(n)** | ✅ Optimal! One pass with LPS |
| Brute Force | O(n × d) | O(n) | Check all divisors d |
| String Trick | O(n) | O(n) | But with large constant factor |
| Regex | O(n²) worst | O(n) | Slow for repeating patterns |

**d** = number of divisors of n (usually very small)

### 🎯 Why Other Approaches Fail (Or Are Slower)

#### ❌ Brute Force Divisor Check
```java
// Slower approach that many use
public boolean repeatedSubstringPattern(String s) {
    int n = s.length();
    
    // Try all possible pattern lengths
    for (int len = 1; len <= n / 2; len++) {
        if (n % len == 0) {  // Only check valid lengths
            String pattern = s.substring(0, len);
            
            // Check if repeating this pattern gives s
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n / len; i++) {
                sb.append(pattern);
            }
            
            if (sb.toString().equals(s)) {
                return true;
            }
        }
    }
    return false;
}

// Time: O(n × d) where d = divisor count
// Space: O(n) for StringBuilder
// SLOWER because it reconstructs strings multiple times!
```

#### ❌ String Trick (Elegant but Slower Constant)
```java
// Popular trick: (s+s).substring(1).contains(s)
// Why it works: if s is repeated, s will appear in doubled string
public boolean repeatedSubstringPattern(String s) {
    String doubled = s + s;
    return doubled.substring(1, doubled.length() - 1).contains(s);
}

// Time: O(n) in best case, but indexOf() has large constant
// Space: O(n) for creating new string
// SLOWER in practice due to substring and indexOf overhead!
```

#### ✅ KMP Approach (BEST!)
```java
// KMP: Direct O(n), no extra string operations
// Single pass through the string
// LPS array directly reveals the pattern structure
```

### 🧪 Test Cases for This Problem

```java
// Case 1: Simple repeating pattern
"abab" → LPS[3]=2, pattern_len=2, 4%2=0 → true ✅

// Case 2: Longer pattern
"abcabcabcabc" → LPS[11]=9, pattern_len=3, 12%3=0 → true ✅

// Case 3: Non-repeating
"aba" → LPS[2]=1, pattern_len=2, 3%2≠0 → false ✅

// Case 4: Single character (can't repeat)
"a" → LPS[0]=0, returns false ✅

// Case 5: All same character
"aaaa" → LPS[3]=3, pattern_len=1, 4%1=0 → true ✅

// Case 6: Two same chars
"aa" → LPS[1]=1, pattern_len=1, 2%1=0 → true ✅

// Case 7: No pattern
"abcd" → LPS[3]=0, returns false ✅

// Case 8: Large repeating
"abcdefabcdefabcdef" → pattern_len=6, 18%6=0 → true ✅

// Case 9: Almost repeating (last char different)
"abcab" → LPS[4]=2, pattern_len=3, 5%3≠0 → false ✅

// Case 10: Triple repetition
"xyzxyzxyz" → pattern_len=3, 9%3=0 → true ✅
```

### ⚠️ Common Mistakes in This Problem

#### ❌ Mistake 1: Forgetting the Divisibility Check
```java
// WRONG: Only checking if LPS[n-1] != 0
if (lps[n - 1] != 0) {
    return true;  // ❌ "abc" pattern in "abcab" gives LPS[4]=2
}                 // ❌ But 5 % 3 ≠ 0, so NOT valid!

// RIGHT: Must also check divisibility
int patternLen = n - lps[n - 1];
return n % patternLen == 0;  // ✅ Complete check
```

#### ❌ Mistake 2: Off-by-one in LPS Array Access
```java
// WRONG: Using lps[n] (out of bounds)
if (lps[n] == 0) {  // ❌ IndexOutOfBoundsException
    return false;
}

// RIGHT: Use lps[n-1]
if (lps[n - 1] == 0) {  // ✅ Last element
    return false;
}
```

#### ❌ Mistake 3: Using the Pattern Length Incorrectly
```java
// WRONG: Pattern length calculation
int patternLen = lps[n - 1];  // ❌ This is the overlap, not pattern len!

// RIGHT: Formula
int patternLen = n - lps[n - 1];  // ✅ n minus overlap = actual pattern
```

### 🎓 Why This Solution is Elegant

1. **Single Array**: No extra data structures, just LPS
2. **One Pass**: O(n) time, can't do better
3. **Mathematical**: Reveals pattern structure inherently
4. **No Reconstruction**: Doesn't build strings repeatedly
5. **Insight Bonus**: Teaches LPS array deeply

### 🔄 Variations of This Problem

| Problem | Approach |
|---------|----------|
| **Find the repeating unit itself** | `s.substring(0, n - lps[n-1])` |
| **Count repetitions** | `n / (n - lps[n-1])` |
| **Minimum period** | `n - lps[n-1]` |
| **Check if has period k** | Check if `lps[n-1] >= n - k` |
| **Construct string from pattern** | Already given as string in this problem |

### 💡 Interview Follow-ups

**Q: Can you solve without KMP?**  
A: Yes, but it's O(n × d) with divisor checks or uses string tricks with higher constants.

**Q: What if string contains spaces/special chars?**  
A: KMP still works! LPS doesn't care about character types.

**Q: What if we need the actual repeating pattern?**  
A: Use `s.substring(0, n - lps[n-1])` to extract it.

**Q: Can we use this for multiple queries?**  
A: Yes! Precompute LPS once, reuse for multiple checks.

---

## 📊 Performance Comparison

### Example: Finding "search" in 1M character text

```
Naive Approach:       ~50,000,000 operations (worst case)
KMP:                  ~1,000,006 operations ✅
String.indexOf():     ~1,000,006 operations (optimized)
```

KMP is **50x faster** for worst-case scenarios!

---

## 🚀 Advanced Tips for Contests

1. **Precompute LPS**: If multiple queries on same pattern, compute once
2. **Use StringBuilder**: For large texts, avoid repeated string concatenation
3. **Handle Boundaries**: Pattern longer than text should return -1 immediately
4. **Optimize for Multiple Patterns**: Use Aho-Corasick (multiple KMP) for finding multiple patterns
5. **Check Time Limit**: For simple cases, sometimes `.indexOf()` is faster to code and sufficient
6. **Debug with Print LPS**: Print LPS array to verify correctness
7. **Consider Variants**: Rolling hash (Rabin-Karp) sometimes preferred if pattern is very long

---

## 📖 Key Formulas to Remember

| Formula | Usage |
|---------|-------|
| `LPS[i]` | Max length of prefix=suffix for `pattern[0...i]` |
| `jIdx = lps[jIdx - 1]` | Jump on mismatch (doesn't increment iIdx) |
| `Match position` | `iIdx - jIdx` when `jIdx == pattern.length()` |
| **Total Time** | **O(n) + O(m)** where n = text length, m = pattern length |

---

## ❓ Interview/Contest Questions

1. **Why doesn't KMP increment `iIdx` on mismatch?**
   → Because suffix of matched prefix can be prefix of pattern. Incrementing would skip potential matches.

2. **What happens with `pattern = "aaa"` in `text = "aaaa"`?**
   → LPS = [0, 1, 2]. Matches at positions 0, 1 due to overlapping.

3. **Can KMP handle patterns with wildcards?**
   → No, KMP requires exact character matching. Use regex or other algorithms.

4. **What is the worst-case pattern for naive approach?**
   → `text = "aaa...ab"` and `pattern = "aaa...ac"`. Naive: O(n*m), KMP: O(n).

5. **Why is LPS array called "failure function"?**
   → It tells where to "fail back" on mismatch without losing information.

---

## 🔗 Related Algorithms

- **Rabin-Karp**: Faster for multiple patterns, but probabilistic
- **Z-Algorithm**: Alternative pattern matching with similar O(n+m)
- **Aho-Corasick**: For multiple pattern matching
- **Boyer-Moore**: Generally faster in practice for larger alphabets

---

## 💡 Remember Before Submitting

✅ Test overlapping matches  
✅ Test pattern at boundaries  
✅ Test pattern longer than text  
✅ Print LPS array for debugging  
✅ Verify position calculation  
✅ Check for off-by-one errors  
✅ Handle empty strings  

---

**Last Updated**: 2026-04-30  
**Status**: Ready for competitive programming! 🎯
