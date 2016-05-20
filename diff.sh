#!/bin/sh

#### 引数 ######
#
# $1 = type1 : debug or experiment
# $2 = directory1 : ディレクトリ名
# $3 = date1 : 日付(yyyy-MM-dd)
# $4 = time1 : 時間(HH-mm)
# $5 = type2 : debug or experiment
# $6 = directory2 : ディレクトリ名
# $7 = date2 : 日付(yyyy-MM-dd)
# $8 = time2 : 時間(HH-mm)
#
################

type1=$1
directory1=$2
date1=$3
time1=$4
type2=$5
directory2=$6
date2=$7
time2=$8

cmn_path="${HOME}/Box Sync/Personal/research"
path1="${cmn_path}/${type1}/${date1}/${directory1}"
path2="${cmn_path}/${type2}/${date2}/${directory2}"

tmp="${PWD}/tmp"

trim_path="${HOME}/BoxSync/Personal/research"



trim() {
	local word=$1
	local trimed=`echo "$word" | sed -e "s/ //"`
	echo "$trimed"
}

restore() {
	local word=$1
	local origin=`echo "$word" | sed -e "s%$trim_path%$cmn_path%"`
	echo "$origin"
}



get_dir() {
	local dir=$1
	local time=$2
	for file in "$dir"/*
	do
		if [ -d "$file" ]; then
			get_dir "$file" $time
		elif [ -f "$file" ]; then
			if [[ "$file" = *.csv ]]; then
				# ファイルのパスから一時的にスペースを除く
				local file_tmp=`trim "$file"`
				if [ `echo "$file_tmp" | grep "$time"` ]; then
					f=`basename "$file_tmp"`
					echo "$file_tmp" | sed -e "s%/$f%%"
					break
				fi
			fi
		fi
	done
}

search() {
	for dir in "$path1"/*
	do
		local d=`basename "$dir"`

		# diffを取るファイルを含むディレクトリを取得
		# 戻り値はスペースを覗いたパス
		local ret1=(`get_dir "$path1/$d" $time1`)
		local ret2=(`get_dir "$path2/$d" $time2`)
		#get_dir "$path1/$d" $time1


		for((i=0;i<${#ret1[*]};i++))
		do

			# ファイル名に指定の時間が含まれるもののみを取得
			# パスをスペースのあるものに復元
			local f1=`restore "${ret1[$i]}"`
			local f2=`restore "${ret2[$i]}"`
			# 戻り値はスペースを除いたパス
			files1=(`validate_file "$f1" $time1`)
			files2=(`validate_file "$f2" $time2`)

			# 要素数が最小の方の要素数を取得
			end=`min ${#files1[*]} ${#files2[*]}`

			for((j=0;j<$end;j++))
			do
				# 比較するファイルが適切かチェック
				success=`check "${files1[$j]}" "${files2[$j]}"`
				if [ $success -eq 0 ]; then
					# パスをスペースのあるものに復元
					f1=`restore "${files1[$j]}"`
					f2=`restore "${files2[$j]}"`
					diff_files "$f1" "$f2"
				else
					echo "ERROR!!!"
					echo "$f1"
					echo "$f2"
					break
				fi
			done

		done
	done
}

validate_file() {
	local dir=$1
	local time=$2
	for file in "$dir"/*
	do
		# ファイルのパスから一時的にスペースを除く
		local file_tmp=`trim "$file"`
		if [ `echo "$file_tmp" | grep $time` ]; then
			echo "$file_tmp"
		fi
	done
}

min() {
	if [ $1 -le $2 ]; then
		echo $1
	else
		echo $2
	fi
}

check() {
	local file1=$1
	local file2=$2

	# ファイル名からターン数、日付、時間、ディレクトリ名を取り除く
	local f1=`echo "$file1" | sed -e "s%$date1%%" -e "s%$time1%%" -e "s%$directory1%%" | sed -E "s%/[0-9]+t%%"`
	local f2=`echo "$file2" | sed -e "s%$date2%%" -e "s%$time2%%" -e "s%$directory2%%" | sed -E "s%/[0-9]+t%%"`

	if [ $f1 = $f2 ]; then
		echo 0
	else
		echo 1
	fi
}

diff_files() {
	local path1=$1
	local path2=$2

	local tmp_f1=$tmp/`echo "$path1" | sed -e "s%$cmn_path/%%"`
	local tmp_f2=$tmp/`echo "$path2" | sed -e "s%$cmn_path/%%"`

	# tmpディレクトリ、ファイル作成
	local f1=`basename "$path1"`
	local f2=`basename "$path2"`
	local dir1=`echo "$tmp_f1" | sed -e "s%/$f1%%"`
	local dir2=`echo "$tmp_f2" | sed -e "s%/$f2%%"`
	mkdir -p "$dir1"
	mkdir -p "$dir2"
	touch "$tmp_f1"
	touch "$tmp_f2"
	
	# 文字コードをUTF-8にして別ファイルに書き出し
	nkf -w "$path1" > "$tmp_f1"
	nkf -w "$path2" > "$tmp_f2"

	# diffをとる
	git diff --no-index "$tmp_f1" "$tmp_f2"
}

mkdir $tmp
search
rm -rf $tmp
