import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../models/children.dart';

class ChildrenScreen extends StatefulWidget {
  const ChildrenScreen({super.key});

  @override
  State<ChildrenScreen> createState() => _ChildrenScreenState();
}

class _ChildrenScreenState extends State<ChildrenScreen> {
  List<Children> children = [];
  bool isLoading = true;
  String error = '';

  @override
  void initState() {
    super.initState();
    _fetchChildren();
  }

  Future<void> _fetchChildren() async {
    try {
      setState(() {
        isLoading = true;
        error = '';
      });
      
      final fetchedChildren = await ApiService.getChildren();
      
      setState(() {
        children = fetchedChildren;
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        error = e.toString();
        isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Children Available'),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _fetchChildren,
          ),
        ],
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : error.isNotEmpty
              ? Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Icon(Icons.error, color: Colors.red, size: 50),
                      const SizedBox(height: 20),
                      Text(
                        'Error: $error',
                        style: const TextStyle(color: Colors.red),
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 20),
                      ElevatedButton(
                        onPressed: _fetchChildren,
                        child: const Text('Retry'),
                      ),
                    ],
                  ),
                )
              : children.isEmpty
                  ? const Center(
                      child: Text(
                        'No children available',
                        style: TextStyle(fontSize: 18, color: Colors.grey),
                      ),
                    )
                  : RefreshIndicator(
                      onRefresh: _fetchChildren,
                      child: ListView.builder(
                        padding: const EdgeInsets.all(16),
                        itemCount: children.length,
                        itemBuilder: (context, index) {
                          final child = children[index];
                          return Card(
                            elevation: 3,
                            margin: const EdgeInsets.only(bottom: 16),
                            child: Padding(
                              padding: const EdgeInsets.all(16),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Row(
                                    children: [
                                      CircleAvatar(
                                        backgroundColor: Colors.blue[100],
                                        child: Text(
                                          '${index + 1}',
                                          style: const TextStyle(
                                            fontWeight: FontWeight.bold,
                                          ),
                                        ),
                                      ),
                                      const SizedBox(width: 16),
                                      Expanded(
                                        child: Column(
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                              '${child.name} ${child.surname}',
                                              style: const TextStyle(
                                                fontSize: 18,
                                                fontWeight: FontWeight.bold,
                                              ),
                                            ),
                                            Text(
                                              'Age: ${child.age} years',
                                              style: TextStyle(
                                                color: Colors.grey[600],
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                    ],
                                  ),
                                  const SizedBox(height: 12),
                                  Row(
                                    children: [
                                      const Icon(Icons.person, size: 16),
                                      const SizedBox(width: 8),
                                      Text(
                                        'Race: ${child.description}',
                                        style: TextStyle(
                                          color: Colors.grey[700],
                                        ),
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ),
                          );
                        },
                      ),
                    ),
    );
  }
}