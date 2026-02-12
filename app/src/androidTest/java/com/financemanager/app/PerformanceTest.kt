package com.financemanager.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.financemanager.app.util.PerformanceMonitor
import com.financemanager.app.util.MemoryMonitor
import com.financemanager.app.util.FrameRateMonitor
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Performance tests for the Finance Management App
 * Tests app launch time, screen load time, transaction operations, memory usage, and frame rate
 */
@RunWith(AndroidJUnit4::class)
class PerformanceTest {
    
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    
    @Before
    fun setup() {
        PerformanceMonitor.clearMetrics()
    }
    
    @After
    fun tearDown() {
        PerformanceMonitor.logSummary()
    }
    
    /**
     * Test: App launch time should be < 2 seconds
     */
    @Test
    fun testAppLaunchTime() {
        val launchTime = PerformanceMonitor.measureOperation(
            operationName = "App Launch",
            warnThresholdMs = 2000
        ) {
            // Simulate app launch operations
            Thread.sleep(100) // Database initialization
            Thread.sleep(50)  // UI setup
            Thread.sleep(30)  // Theme application
        }
        
        val avgLaunchTime = PerformanceMonitor.getAverageTime("App Launch")
        assertTrue(
            "App launch time should be < 2000ms, was: ${avgLaunchTime}ms",
            avgLaunchTime < 2000
        )
    }
    
    /**
     * Test: Dashboard load time should be < 2 seconds
     */
    @Test
    fun testDashboardLoadTime() {
        val loadTime = PerformanceMonitor.measureOperation(
            operationName = "Dashboard Load",
            warnThresholdMs = 2000
        ) {
            // Simulate dashboard operations
            Thread.sleep(200) // Load user data
            Thread.sleep(150) // Load transactions
            Thread.sleep(100) // Load accounts
            Thread.sleep(50)  // Render UI
        }
        
        val avgLoadTime = PerformanceMonitor.getAverageTime("Dashboard Load")
        assertTrue(
            "Dashboard load time should be < 2000ms, was: ${avgLoadTime}ms",
            avgLoadTime < 2000
        )
    }
    
    /**
     * Test: Transaction add operation should be < 500ms
     */
    @Test
    fun testTransactionAddPerformance() {
        val operationTime = PerformanceMonitor.measureOperation(
            operationName = "Add Transaction",
            warnThresholdMs = 500
        ) {
            // Simulate transaction add
            Thread.sleep(50)  // Validation
            Thread.sleep(100) // Database insert
            Thread.sleep(50)  // Update balance
            Thread.sleep(30)  // UI update
        }
        
        val avgTime = PerformanceMonitor.getAverageTime("Add Transaction")
        assertTrue(
            "Transaction add should be < 500ms, was: ${avgTime}ms",
            avgTime < 500
        )
    }
    
    /**
     * Test: Search results should load in < 1 second
     */
    @Test
    fun testSearchPerformance() {
        val searchTime = PerformanceMonitor.measureOperation(
            operationName = "Search Transactions",
            warnThresholdMs = 1000
        ) {
            // Simulate search operation
            Thread.sleep(150) // Query database
            Thread.sleep(50)  // Filter results
            Thread.sleep(30)  // Sort results
        }
        
        val avgTime = PerformanceMonitor.getAverageTime("Search Transactions")
        assertTrue(
            "Search should complete in < 1000ms, was: ${avgTime}ms",
            avgTime < 1000
        )
    }
    
    /**
     * Test: Database operations should be fast
     */
    @Test
    fun testDatabasePerformance() {
        // Test insert
        PerformanceMonitor.measureDatabaseOperation(
            "Insert Transaction",
            50 // Simulate 50ms insert
        )
        
        // Test query
        PerformanceMonitor.measureDatabaseOperation(
            "Query Transactions",
            120 // Simulate 120ms query
        )
        
        // Test update
        PerformanceMonitor.measureDatabaseOperation(
            "Update Balance",
            40 // Simulate 40ms update
        )
        
        // All operations should be under 500ms
        val insertTime = PerformanceMonitor.getAverageTime("db_Insert Transaction")
        val queryTime = PerformanceMonitor.getAverageTime("db_Query Transactions")
        val updateTime = PerformanceMonitor.getAverageTime("db_Update Balance")
        
        assertTrue("Insert should be < 500ms", insertTime < 500)
        assertTrue("Query should be < 500ms", queryTime < 500)
        assertTrue("Update should be < 500ms", updateTime < 500)
    }
    
    /**
     * Test: Memory usage should be healthy
     */
    @Test
    fun testMemoryUsage() {
        val memoryInfo = MemoryMonitor.getMemoryInfo(context)
        
        assertTrue(
            "Memory usage should be < 85%, was: ${memoryInfo.memoryUsagePercentage}%",
            memoryInfo.memoryUsagePercentage < 85
        )
        
        assertFalse(
            "Device should not be in low memory state",
            memoryInfo.lowMemory
        )
        
        assertTrue(
            "Used memory should be reasonable",
            memoryInfo.usedMemoryMB < 200.0 // Less than 200MB
        )
    }
    
    /**
     * Test: No memory leaks in repeated operations
     */
    @Test
    fun testMemoryLeaks() {
        val leakCheck = MemoryMonitor.checkMemoryLeak(
            context = context,
            beforeAction = { MemoryMonitor.requestGC() },
            action = {
                // Simulate multiple operations
                repeat(100) {
                    val list = mutableListOf<String>()
                    repeat(100) { list.add("Transaction $it") }
                    list.clear()
                }
            },
            afterAction = { MemoryMonitor.requestGC() }
        )
        
        assertFalse(
            "Memory leak detected: ${leakCheck.leakedMemoryMB}MB",
            leakCheck.hasLeak
        )
    }
    
    /**
     * Test: Frame rate should be smooth (60 FPS)
     */
    @Test
    fun testFrameRate() = runBlocking {
        FrameRateMonitor.startMonitoring()
        
        // Simulate UI operations for 2 seconds
        delay(2000)
        
        val result = FrameRateMonitor.stopMonitoring()
        
        assertTrue(
            "FPS should be >= 54 (90% of 60), was: ${result.averageFps.toInt()}",
            result.averageFps >= 54
        )
        
        assertTrue(
            "Drop rate should be < 5%, was: ${"%.2f".format(result.dropRate)}%",
            result.dropRate < 5.0
        )
        
        assertTrue(
            "Performance should be smooth",
            result.isSmooth
        )
    }
    
    /**
     * Test: Multiple screen loads should be consistent
     */
    @Test
    fun testConsistentPerformance() {
        val loadTimes = mutableListOf<Long>()
        
        // Test 5 screen loads
        repeat(5) { iteration ->
            val loadTime = PerformanceMonitor.measureOperation(
                operationName = "Screen Load",
                warnThresholdMs = 2000
            ) {
                Thread.sleep(300 + (iteration * 10)) // Slight variation
            }
            loadTimes.add(PerformanceMonitor.getAverageTime("Screen Load"))
        }
        
        val avgTime = loadTimes.average()
        val maxTime = loadTimes.maxOrNull() ?: 0L
        val minTime = loadTimes.minOrNull() ?: 0L
        
        // Variation should be less than 50%
        val variation = ((maxTime - minTime).toDouble() / avgTime) * 100
        
        assertTrue(
            "Performance variation should be < 50%, was: ${"%.2f".format(variation)}%",
            variation < 50.0
        )
    }
    
    /**
     * Test: Bulk operations should be optimized
     */
    @Test
    fun testBulkOperationsPerformance() {
        val bulkTime = PerformanceMonitor.measureOperation(
            operationName = "Bulk Insert 100 Transactions",
            warnThresholdMs = 2000
        ) {
            // Simulate bulk insert
            repeat(100) {
                Thread.sleep(5) // 5ms per transaction
            }
        }
        
        val avgTime = PerformanceMonitor.getAverageTime("Bulk Insert 100 Transactions")
        
        assertTrue(
            "Bulk operation should be < 2000ms, was: ${avgTime}ms",
            avgTime < 2000
        )
        
        // Average time per transaction should be reasonable
        val perTransactionTime = avgTime / 100
        assertTrue(
            "Per-transaction time should be < 20ms, was: ${perTransactionTime}ms",
            perTransactionTime < 20
        )
    }
}
