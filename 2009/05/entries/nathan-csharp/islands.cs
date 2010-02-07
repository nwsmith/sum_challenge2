using System;
using System.Collections.Generic;
using System.Collections;
using System.Text;
using System.IO;

namespace Nathan {
  public class Program { 
    public static void Main(string[] args) {
      if (args.Length != 1 ) {
        Console.WriteLine("Usage: islands.exe <filename>");
        return; 
      }
      World world = new FileFormatter(args[0]).BuildWorld();
      Explorer explorer = new Explorer();
      Map map = explorer.Explore(world);
      PrintOutput(map);
    }

    private static void PrintOutput(Map map) {
      Dictionary<char, int> continentInfo = map.ContinentInfo();
      Console.WriteLine("There are {0} continents", continentInfo.Count);
      int i = 0;
      foreach (KeyValuePair<char, int> kvp in continentInfo) {
        Console.WriteLine("Continent {0} has area {1}", ++i, kvp.Value);
      }
    }
  }

  public class Point {
    private int row;
    private int col;

    public Point(int col, int row) {
      this.col = col;
      this.row = row;
    }

    public int Row { get { return row; } }
    public int Col { get { return col; } }
  }

  public class Grid<T> {
    protected T[,] content;
    protected int width;
    protected int height;

    public Grid(int width, int height) {
      content = new T[width, height];
      this.width = width;
      this.height = height;
    }

    public Grid(T[,] content) {
      this.content = content;
      this.height = content.GetLength(1);
      this.width = content.GetLength(0);
    }

    public int Width {get { return width; }}
    public int Height {get { return height; }}

    public T GetValueAt(Point p) {
      return content[p.Col, p.Row];
    }

    public virtual void SetValueAt(Point p, T val) {
      content[p.Col, p.Row] = val;
    }

    public IEnumerable Traverse() {
      for (int r = 0; r < height; ++r) {
        for (int c = 0; c < width; ++c) {
          yield return new Point(c, r);
        }
      }
    }

    public override string ToString() {
      StringBuilder s = new StringBuilder();
      for (int r = 0; r < height; ++r) {
        for (int c = 0; c < width; ++c) {
          s.Append(content[c, r]);  
        }
        s.Append(System.Environment.NewLine);
      }
      return s.ToString();
    }
  }

  public class Map: Grid<char> {
    public Map(World world) : base(world.Width, world.Height) {
    }

    public Dictionary<char, int> ContinentInfo() {
      Dictionary<char, int> continentInfo = new Dictionary<char, int>();
      foreach (Point p in Traverse()) {
        char c = GetValueAt(p);
        if (c == '-') {
          continue;
        }
        if (!continentInfo.ContainsKey(c)) {
          continentInfo[c] = 0;
        }
        ++continentInfo[c];
      }
      return continentInfo;
    }

    public IEnumerable EachConnected(Point p) {
      int c = p.Col;
      int r = p.Row;

      if (c > 0 && r > 0 && (content[c-1,r-1] != '-')) {
        yield return new Point(c-1,r-1);
      }
      if (r > 0 && (content[c, r-1] != '-')) {
        yield return new Point(c, r-1);
      }
      if (c < (width-1) && r > 0 && (content[c+1,r-1] != '-')) {
        yield return new Point(c+1,r-1);
      }
      if (c > 0 && (content[c-1,r] != '-')) {
        yield return new Point(c-1, r);
      }
    }
  }

  public class World: Grid<bool> {
    public World(bool[,] content) : base(content) {
    }

    public IEnumerable EachAdjacent(Point p) {
      int c = p.Col;
      int r = p.Row;

      foreach (int x in new int[]{-1, 0, 1}) {
        foreach (int y in new int[]{-1, 0, 1}) {
          if ((c+x > 0 && c+x < (width-1)) && (r+y > 0 && r+y < (height-1)) && content[c+x,r+y]) {
            yield return new Point(c+x,r+y);
          }
        }
      }
    }
  }

  public class Explorer {
    public Explorer() {
    }

    public Map Explore(World world) {
      Map map = new Map(world);
      Dictionary<char, int> info = new Dictionary<char, int>();
      char name = 'A';

      foreach (Point p in world.Traverse()) {
        if (world.GetValueAt(p)) {
          foreach (Point connected in map.EachConnected(p)) {
            // If we're connected to an existing continent, use that name
            name = map.GetValueAt(connected);
          }
          map.SetValueAt(p, name);
          if (!info.ContainsKey(name)) {
            info[name] = 0;
          }
          ++info[name];
          ExploreNeighbours(world, map, p, name);
        } else {
          do {
            ++name;
          } while (info.ContainsKey(name));
          map.SetValueAt(p, '-');
        }
      }
      return map;
    }

    // Because we're naive about starting a new continent, this picks up that land
    // i.e.:
    // for +++---+++---
    //     --+++++++---
    //
    // This will make sure the right tip gets added to the proper continent
    private void ExploreNeighbours(World world, Map map, Point point, char name) {
      foreach (Point p in world.EachAdjacent(point)) {
        char old = map.GetValueAt(p);
        if (old != name) {
          map.SetValueAt(p, name);
          ExploreNeighbours(world, map, p, name);
        }
      }
    }
  }

  public class FileFormatter {
    private string filename;

    public FileFormatter(string filename) {
      this.filename = filename;
    }

    public World BuildWorld() {
      List<string> rawData = ReadFile(filename);
      int height = rawData.Count;
      int width = rawData[0].Length;

      bool[,] world = new bool[width, height];

      int r = 0;

      rawData.ForEach(delegate(string s) {
        int c = 0;
        foreach (char ch in s) {
          world[c++, r] = (ch == '+');
        }
        ++r;
      });

      return new World(world);
    }

    private List<string> ReadFile(string filename) {
      List<string> all = new List<string>();
      StreamReader reader = new StreamReader(filename);
      string line;

      while ((line = reader.ReadLine()) != null) {
        all.Add(line);
      }

      reader.Close();

      return all;
    }
  }
}
