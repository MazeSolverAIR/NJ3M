#ifndef LIST_H
#define LIST_H

//Kreiranje vezane liste
class List {
private:
	typedef struct node
	{
		char* data;
		node* next;
		int elementBroj;
	}* nodePtr;

	nodePtr head; //glava
	nodePtr curr; //trenutni pokazivač
	nodePtr temp; //privremeni pokazivač

public: //funkcije liste
	List();
	void AddNode(char* addData);
	void DeleteNode(int pozicijaElementa);
	char* PrintElement(int pozicijaElementa);
	int brojElemenata();

};

#endif // !LIST_H
